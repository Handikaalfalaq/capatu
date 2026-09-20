package com.capatu.shoe_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceLogSearchRequest extends PageableRequest {

    private Long shoeId;
    private Long actionRefId;

    private LocalDate actionDateFrom;
    private LocalDate actionDateTo;

}
