package com.parkingsystem.infrastructure.api.exception;

import com.parkingsystem.domain.errors.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Converter domain exceptions to API exception with http-statuses
 */
@RestControllerAdvice
@Slf4j
class HttpExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    @ResponseBody
    ResponseEntity<ApiErrorMessage> handleRequestErrorMyException(DomainException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus().getCode())
                .body(new ApiErrorMessage(exception.getDescription()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ApiErrorMessage> handleRequestErrorMyException(Exception exception) {
        log.error("Internal server error. " + exception.getLocalizedMessage());
        return ResponseEntity
                .status(500)
                .body(new ApiErrorMessage("Internal server error. " + exception.getLocalizedMessage()));
    }
}
