package com.capatu.reminder_service.exception;

import com.capatu.reminder_service.constant.Constants;
import com.capatu.reminder_service.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return build(status, ex.getReason() != null ? ex.getReason() : status.getReasonPhrase(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Parameter '" + ex.getName() + "' tidak valid", request);
    }

    @ExceptionHandler(ShoeServiceUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleShoeServiceUnavailable(ShoeServiceUnavailableException ex, HttpServletRequest request) {
        log.warn("shoe-service tidak dapat dihubungi: {}", ex.getMessage());
        return build(HttpStatus.SERVICE_UNAVAILABLE, Constants.SHOE_SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleOther(Exception ex, HttpServletRequest request) {
        if (ex instanceof ErrorResponse springError) {
            return build(HttpStatus.valueOf(springError.getStatusCode().value()), springError.getBody().getDetail(), request);
        }
        log.error("Kesalahan tak terduga", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server", request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(),
                message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
