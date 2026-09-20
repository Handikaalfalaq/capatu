package com.capatu.shoe_service.service;

import com.capatu.shoe_service.dto.response.StatsResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface StatsService {

    StatsResponse.Summary summary(LocalDate from, LocalDate to);

    StatsResponse.Ranking ranking(LocalDate from, LocalDate to);

    StatsResponse.Rotation rotation(Integer days);

    StatsResponse.ShoeLife shoeLife(BigDecimal threshold);
}
