package com.capatu.shoe_service.service.impl;

import com.capatu.shoe_service.constant.Constants;
import com.capatu.shoe_service.dto.response.ActivityLogRow;
import com.capatu.shoe_service.dto.response.ShoeRanking;
import com.capatu.shoe_service.dto.response.StatsResponse;
import com.capatu.shoe_service.repository.StatsRepository;
import com.capatu.shoe_service.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional(readOnly = true)
    public StatsResponse.Summary summary(LocalDate from, LocalDate to) {
        LocalDate end = periodEnd(to);
        LocalDate start = periodStart(from, end);

        List<ActivityLogRow> rows = statsRepository.findActivityRows(start, end);

        BigDecimal totalDistanceKm = rows.stream()
                .map(ActivityLogRow::getDistanceKm)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<StatsResponse.ActivityBreakdown> byActivity = rows.stream()
                .collect(Collectors.groupingBy(ActivityLogRow::getActivity,
                        Collectors.reducing(BigDecimal.ZERO, ActivityLogRow::getDistanceKm, BigDecimal::add)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(entry -> new StatsResponse.ActivityBreakdown(
                        entry.getKey(), entry.getValue(), percent(entry.getValue(), totalDistanceKm)))
                .toList();

        List<StatsResponse.ShoeUsage> shoes = statsRepository.summarizeByShoe(start, end).stream()
                .map(shoe -> new StatsResponse.ShoeUsage(shoe.getShoeId(), shoe.getShoeName(), shoe.getShoeType(),
                        shoe.getUsageCount(), shoe.getTotalDistanceKm(), shoe.getAvgDistanceKm(), shoe.getLastUsedAt()))
                .toList();

        return new StatsResponse.Summary(start, end, totalDistanceKm, shoes, byActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public StatsResponse.Ranking ranking(LocalDate from, LocalDate to) {
        LocalDate end = periodEnd(to);
        LocalDate start = periodStart(from, end);

        List<StatsResponse.ShoeRank> shoes = statsRepository.rankShoes(start, end).stream()
                .map(shoe -> new StatsResponse.ShoeRank(shoe.getShoeId(), shoe.getShoeName(),
                        shoe.getUsageCount(), shoe.getMostUsedRank(), shoe.getLeastUsedRank()))
                .toList();

        return new StatsResponse.Ranking(start, end, shoes);
    }

    @Override
    @Transactional(readOnly = true)
    public StatsResponse.Rotation rotation(Integer days) {
        int period = days != null ? days : Constants.DEFAULT_STATS_DAYS;
        if (period < 1 || period > Constants.MAX_STATS_DAYS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.INVALID_STATS_DAYS.formatted(Constants.MAX_STATS_DAYS));
        }
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(period);

        List<ShoeRanking> ranking = statsRepository.rankShoes(start, end);
        long totalUsage = ranking.stream().mapToLong(ShoeRanking::getUsageCount).sum();

        List<StatsResponse.RotationShare> shares = ranking.stream()
                .map(shoe -> {
                    BigDecimal percent = percent(BigDecimal.valueOf(shoe.getUsageCount()), BigDecimal.valueOf(totalUsage));
                    boolean unhealthy = ranking.size() > 1
                            && percent.compareTo(Constants.UNHEALTHY_ROTATION_PERCENT) > 0;
                    return new StatsResponse.RotationShare(shoe.getShoeId(), shoe.getShoeName(),
                            shoe.getUsageCount(), percent, unhealthy);
                })
                .toList();

        List<StatsResponse.IdleShoeItem> idleShoes = statsRepository.findIdleShoes(start).stream()
                .map(shoe -> new StatsResponse.IdleShoeItem(shoe.getShoeId(), shoe.getShoeName(), shoe.getLastUsedAt()))
                .toList();

        return new StatsResponse.Rotation(period, start, end, shares, idleShoes);
    }

    @Override
    @Transactional(readOnly = true)
    public StatsResponse.ShoeLife shoeLife(BigDecimal threshold) {
        BigDecimal limit = threshold != null ? threshold : Constants.DEFAULT_LIFE_THRESHOLD;
        if (limit.signum() < 0 || limit.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_THRESHOLD);
        }

        List<StatsResponse.ShoeLifeItem> shoes = statsRepository.findShoeLifespans(limit).stream()
                .map(shoe -> new StatsResponse.ShoeLifeItem(shoe.getShoeId(), shoe.getShoeName(), shoe.getShoeType(),
                        shoe.getTotalDistanceKm(), shoe.getTargetLifespanKm(), shoe.getRemainingPercent(),
                        shoe.getFeatures()))
                .toList();

        return new StatsResponse.ShoeLife(limit, shoes);
    }

    private LocalDate periodEnd(LocalDate to) {
        return to != null ? to : LocalDate.now();
    }

    private LocalDate periodStart(LocalDate from, LocalDate end) {
        LocalDate start = from != null ? from : end.withDayOfMonth(1);
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_RANGE.formatted("periode"));
        }
        return start;
    }

    private BigDecimal percent(BigDecimal value, BigDecimal total) {
        if (total.signum() == 0) {
            return BigDecimal.ZERO.setScale(1);
        }
        return value.multiply(BigDecimal.valueOf(100)).divide(total, 1, RoundingMode.HALF_UP);
    }
}
