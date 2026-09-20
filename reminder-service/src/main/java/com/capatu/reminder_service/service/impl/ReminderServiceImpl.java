package com.capatu.reminder_service.service.impl;

import com.capatu.reminder_service.client.ShoeServiceClient;
import com.capatu.reminder_service.constant.Constants;
import com.capatu.reminder_service.dto.response.EvaluationResult;
import com.capatu.reminder_service.dto.response.ReminderResponse;
import com.capatu.reminder_service.entity.ReminderModel;
import com.capatu.reminder_service.enums.ReminderSeverity;
import com.capatu.reminder_service.enums.ReminderStatus;
import com.capatu.reminder_service.repository.ReminderRepository;
import com.capatu.reminder_service.rule.ReminderCandidate;
import com.capatu.reminder_service.rule.ReminderRule;
import com.capatu.reminder_service.rule.RuleContext;
import com.capatu.reminder_service.service.ReminderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ShoeServiceClient shoeServiceClient;
    private final List<ReminderRule> rules;
    private final int rotationDays;

    public ReminderServiceImpl(ReminderRepository reminderRepository,
                               ShoeServiceClient shoeServiceClient,
                               List<ReminderRule> rules,
                               @Value("${capatu.reminder.rotation-days}") int rotationDays) {
        this.reminderRepository = reminderRepository;
        this.shoeServiceClient = shoeServiceClient;
        this.rules = rules;
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
        return ReminderResponse.from(reminderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, Constants.NOT_FOUND.formatted(Constants.RESOURCE_REMINDER))));
    }

    @Override
    @Transactional
    public EvaluationResult evaluate() {
        RuleContext context = new RuleContext(
                shoeServiceClient.fetchShoes(),
                shoeServiceClient.fetchRotation(rotationDays));

        List<ReminderCandidate> candidates = rules.stream()
                .flatMap(rule -> rule.evaluate(context).stream())
                .toList();
        List<ReminderModel> active = reminderRepository.findByStatus(ReminderStatus.ACTIVE);

        int created = 0;
        for (ReminderCandidate candidate : candidates) {
            Optional<ReminderModel> existing = active.stream()
                    .filter(reminder -> isSame(reminder, candidate))
                    .findFirst();

            if (existing.isPresent()) {
                update(existing.get(), candidate);
            } else {
                reminderRepository.save(newReminder(candidate));
                created++;
            }
        }

        List<ReminderModel> resolved = active.stream()
                .filter(reminder -> candidates.stream().noneMatch(candidate -> isSame(reminder, candidate)))
                .toList();
        resolved.forEach(this::resolve);

        return new EvaluationResult(created, resolved.size());
    }

    private boolean isSame(ReminderModel reminder, ReminderCandidate candidate) {
        return reminder.getShoeId().equals(candidate.shoeId()) && reminder.getType() == candidate.type();
    }

    private void update(ReminderModel reminder, ReminderCandidate candidate) {
        reminder.setShoeName(candidate.shoeName());
        reminder.setSeverity(candidate.severity());
        reminder.setMessage(candidate.message());
        reminderRepository.save(reminder);
    }

    private void resolve(ReminderModel reminder) {
        reminder.setStatus(ReminderStatus.RESOLVED);
        reminder.setResolvedAt(LocalDateTime.now());
        reminderRepository.save(reminder);
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
}
