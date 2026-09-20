package com.capatu.shoe_service.dto.response;

import java.time.LocalDate;

public interface IdleShoe {

    Long getShoeId();

    String getShoeName();

    LocalDate getLastUsedAt();
}
