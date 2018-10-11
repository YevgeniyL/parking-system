package com.parkingsystem.infrastructure.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;


/**
 * Converter domain exceptions to API exception with http-statuses
 */
@ControllerAdvice
class HttpExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainToHttpExceptionsConverter.class)
    @ResponseBody
    ResponseEntity<ApiErrorMessage> handleRequestErrorMyException(DomainToHttpExceptionsConverter exception) {
        HttpStatus responseStatus = resolveAnnotatedResponseStatus(exception);
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getDescription());
        return new ResponseEntity<>(apiErrorMessage, responseStatus);
    }

    HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
