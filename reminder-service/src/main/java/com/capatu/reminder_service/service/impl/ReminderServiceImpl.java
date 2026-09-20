package com.capatu.reminder_service.service.impl;

import com.capatu.reminder_service.client.ShoeServiceClient;
import com.capatu.reminder_service.constant.Constants;
import com.capatu.reminder_service.dto.response.EvaluationResult;
import com.capatu.reminder_service.dto.response.ReminderResponse;
import com.capatu.reminder_service.entity.ReminderModel;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderStatus;
import com.capatu.reminder_service.enums.ReminderType;
import com.capatu.reminder_service.repository.ReminderRepository;
import com.capatu.reminder_service.rule.ReminderCandidate;
import com.capatu.reminder_service.rule.ReminderRule;
import com.capatu.reminder_service.rule.RuleContext;
import com.capatu.reminder_service.service.ReminderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReminderServiceImpl implements ReminderService {

    private static final Set<ReminderStatus> OPEN_STATUSES = EnumSet.of(ReminderStatus.ACTIVE, ReminderStatus.ACKNOWLEDGED);

    private final ReminderRepository reminderRepository;
    private final ShoeServiceClient shoeServiceClient;
    private final List<ReminderRule> rules;
    private final TransactionTemplate transactionTemplate;
    private final int rotationDays;

    public ReminderServiceImpl(ReminderRepository reminderRepository,
                               ShoeServiceClient shoeServiceClient,
                               List<ReminderRule> rules,
                               PlatformTransactionManager transactionManager,
                               @Value("${capatu.reminder.rotation-days}") int rotationDays) {
        this.reminderRepository = reminderRepository;
        this.shoeServiceClient = shoeServiceClient;
        this.rules = rules;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.rotationDays = rotationDays;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReminderResponse> findAll(ReminderStatus status, ReminderSeverity severity) {
        ReminderStatus wanted = status != null ? status : ReminderStatus.ACTIVE;

        return reminderRepository.findByStatus(wanted).stream()
                .filter(reminder -> severity == null || reminder.getSeverity() == severity)
                .sorted(Comparator.comparing(ReminderModel::getSeverity).reversed()
                        .thenComparing(ReminderModel::getDetectedAt, Comparator.reverseOrder()))
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReminderResponse findById(Long id) {
        return ReminderResponse.from(findEntity(id));
    }

    @Override
    @Transactional
    public void acknowledge(Long id) {
        ReminderModel reminder = findEntity(id);

        if (reminder.getStatus() == ReminderStatus.RESOLVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, Constants.ALREADY_RESOLVED);
        }
        if (reminder.getStatus() == ReminderStatus.ACTIVE) {
            reminder.setStatus(ReminderStatus.ACKNOWLEDGED);
            reminder.setAcknowledgedAt(LocalDateTime.now());
            reminderRepository.save(reminder);
        }
    }

    @Override
    public synchronized EvaluationResult evaluate() {
        RuleContext context = new RuleContext(
                shoeServiceClient.fetchShoes(),
                shoeServiceClient.fetchRotation(rotationDays));

        List<ReminderCandidate> candidates = rules.stream()
                .flatMap(rule -> rule.evaluate(context).stream())
                .toList();

        return transactionTemplate.execute(status -> synchronize(candidates));
    }

    private EvaluationResult synchronize(List<ReminderCandidate> candidates) {
        Map<ReminderKey, ReminderModel> open = reminderRepository.findByStatusIn(OPEN_STATUSES).stream()
                .collect(Collectors.toMap(
                        reminder -> new ReminderKey(reminder.getShoeId(), reminder.getType()),
                        Function.identity()));

        int created = 0;
        int updated = 0;
        int reactivated = 0;

        for (ReminderCandidate candidate : candidates) {
            ReminderModel existing = open.remove(new ReminderKey(candidate.shoeId(), candidate.type()));

            if (existing == null) {
                reminderRepository.save(newReminder(candidate));
                created++;
                continue;
            }

            boolean escalated = candidate.severity().compareTo(existing.getSeverity()) > 0;
            if (existing.getStatus() == ReminderStatus.ACKNOWLEDGED && escalated) {
                existing.setStatus(ReminderStatus.ACTIVE);
                existing.setAcknowledgedAt(null);
                reactivated++;
            } else {
                updated++;
            }
            existing.setShoeName(candidate.shoeName());
            existing.setSeverity(candidate.severity());
            existing.setMessage(candidate.message());
            reminderRepository.save(existing);
        }

        LocalDateTime now = LocalDateTime.now();
        open.values().forEach(reminder -> {
            reminder.setStatus(ReminderStatus.RESOLVED);
            reminder.setResolvedAt(now);
            reminderRepository.save(reminder);
        });

        return new EvaluationResult(created, updated, reactivated, open.size(), candidates.size());
    }

    private ReminderModel newReminder(ReminderCandidate candidate) {
        ReminderModel reminder = new ReminderModel();
        reminder.setShoeId(candidate.shoeId());
        reminder.setShoeName(candidate.shoeName());
        reminder.setType(candidate.type());
        reminder.setSeverity(candidate.severity());
        reminder.setMessage(candidate.message());
        reminder.setStatus(ReminderStatus.ACTIVE);
        reminder.setDetectedAt(LocalDateTime.now());
        return reminder;
    }

    private ReminderModel findEntity(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, Constants.NOT_FOUND.formatted(Constants.RESOURCE_REMINDER)));
    }

    private record ReminderKey(Long shoeId, ReminderType type) {
    }
}
