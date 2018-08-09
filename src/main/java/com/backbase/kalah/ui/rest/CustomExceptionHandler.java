package com.backbase.kalah.ui.rest;

import com.backbase.kalah.exceptions.KalahException;
import com.backbase.kalah.ui.rest.entity.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String detailsFormat = "[Error] [Message: %s] [Ip: %s] [Path: %s]";
    private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception, HttpServletRequest request) {
        if (exception instanceof KalahException)
            logger.warn(String.format(detailsFormat, exception.getMessage(), request.getRemoteHost(), request.getRequestURI()));
        else logger.error(exception.getMessage(), exception);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                exception.getClass().getName(),
                exception.getMessage(),
                request.getRequestURI()), httpStatus);
    }
}
