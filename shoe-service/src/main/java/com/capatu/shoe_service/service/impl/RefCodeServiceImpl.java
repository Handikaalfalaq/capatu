package com.capatu.shoe_service.service.impl;

import com.capatu.shoe_service.constant.MessageConstants;
import com.capatu.shoe_service.dto.request.RefCodeRequest;
import com.capatu.shoe_service.dto.response.RefCodeResponse;
import com.capatu.shoe_service.entity.RefCodeModel;
import com.capatu.shoe_service.repository.RefCodeRepository;
import com.capatu.shoe_service.service.RefCodeService;
import com.capatu.shoe_service.utils.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefCodeServiceImpl implements RefCodeService {

    private final RefCodeRepository refCodeRepository;

    @Override
    public List<RefCodeResponse> findAllCode(){
        return refCodeRepository.findAllCode();
    }

    @Override
    public List<RefCodeModel> allRefCodeByType(String type){
        return refCodeRepository.allRefCodeByType(type);
    }

    @Override
    public RefCodeModel findById(Long id){
        return refCodeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, MessageConstants.NOT_FOUND.formatted(MessageConstants.RESOURCE_REF_CODE)));
    }

    @Override
    public void create(RefCodeRequest request){
        String type = TypeNameUtils.toType(request.getTypeName());
        String code = TypeNameUtils.toType(request.getCodeName());

        validateUniqueCodeType(request, type, code, null);

        RefCodeModel refCodeModel = new RefCodeModel();
        refCodeModel.setType(type);
        refCodeModel.setTypeName(request.getTypeName());
        refCodeModel.setCode(code);
        refCodeModel.setCodeName(request.getCodeName().trim());
        refCodeModel.setDescription(request.getDescription());
        refCodeModel.setIsActive(request.getIsActive());

        refCodeRepository.save(refCodeModel);
    }

    @Override
    public void update(Long id, RefCodeRequest request){
        RefCodeModel refCodeExisting = findById(id);

        String type = TypeNameUtils.toType(request.getTypeName());
        String code = TypeNameUtils.toType(request.getCodeName());

        validateUniqueCodeType(request, type, code, id);

        refCodeExisting.setType(type);
        refCodeExisting.setTypeName(request.getTypeName());
        refCodeExisting.setCode(code);
        refCodeExisting.setCodeName(request.getCodeName().trim());
        refCodeExisting.setDescription(request.getDescription());
        refCodeExisting.setIsActive(request.getIsActive());

        refCodeRepository.save(refCodeExisting);
    }

    @Override
    public void delete(Long id){
        RefCodeModel refCode = findById(id);

        try {
            refCodeRepository.delete(refCode);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    MessageConstants.IN_USE.formatted(MessageConstants.RESOURCE_REF_CODE));
        }
    }

    private void validateUniqueCodeType(RefCodeRequest request, String type, String code, Long id){
        boolean isDuplicate = refCodeRepository.findByTypeAndCode(type, code)
                .filter(found -> !found.getId().equals(id))
                .isPresent();

        if (isDuplicate) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    MessageConstants.CODE_ALREADY_EXISTS.formatted(request.getCodeName(), request.getTypeName()));
        }
    }
}
