package com.parkingsystem.infrastructure.api.exception;

import com.parkingsystem.domain.errors.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;


/**
 * Converter domain exceptions to API exception with http-statuses
 */
@RestControllerAdvice
@Slf4j
class HttpExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    @ResponseBody
    ResponseEntity<ApiErrorMessage> domainCustomException(DomainException exception, WebRequest request) {
        Integer code = exception.getHttpStatus().getCode();
        HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
        String url = httpServletRequest.getRequestURL().toString();
        String method = httpServletRequest.getMethod();
        String sessionId = request.getSessionId();
        String description = exception.getDescription();
        String loggingDetails = exception.getLoggingDetails();
        log.error(MessageFormat.format(
                "\n\n{0} -- Error in request -> [{1}]:[{2}] with sessionId =[{3}]. HttpCustomCode =[{4}]. \nCause: {5}." + buildLogDetailsPattern(loggingDetails) + "\n",
                LocalDateTime.now(), method, url, sessionId, code, description, buildLogDetails(loggingDetails)
        ));

        return ResponseEntity
                .status(code)
                .body(new ApiErrorMessage(description));
    }

    private String buildLogDetailsPattern(String loggingDetails) {
        return StringUtils.isEmpty(loggingDetails) ? "" : " Details: {6}";
    }

    private String buildLogDetails(String loggingDetails) {
        return StringUtils.isEmpty(loggingDetails) ? "" : loggingDetails;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ApiErrorMessage> domainSystemException(Exception exception) {
        log.error("Internal server error. " + exception.getLocalizedMessage());
        return ResponseEntity
                .status(500)
                .body(new ApiErrorMessage("\nInternal server error. " + exception.getLocalizedMessage()));
    }


}
