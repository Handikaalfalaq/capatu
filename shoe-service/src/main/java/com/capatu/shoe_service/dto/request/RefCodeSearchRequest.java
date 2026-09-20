package com.capatu.shoe_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefCodeSearchRequest extends PageableRequest {

    private String type;

    private String codeName;
}
