package com.capatu.shoe_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoeSearchRequest extends PageableRequest {

    private String name;

    private String brand;

    private Long shoeTypeRefId;

    private Boolean retired;
}
