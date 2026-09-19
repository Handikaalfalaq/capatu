package com.capatu.shoe_service.dto.response;

import com.capatu.shoe_service.entity.RefCodeModel;

public record RefCodeItemResponse(Long id, String type, String typeName, String code, String codeName,
                                  String description, Boolean isActive) {

    public static RefCodeItemResponse from(RefCodeModel model) {
        return new RefCodeItemResponse(model.getId(), model.getType(), model.getTypeName(), model.getCode(),
                model.getCodeName(), model.getDescription(), model.getIsActive());
    }
}
