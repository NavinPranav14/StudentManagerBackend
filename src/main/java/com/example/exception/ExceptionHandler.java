package com.example.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class,
        ServiceException.class})
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        String message;
        if (ex instanceof ServiceException) {
            message = ex.getMessage();
        } else {
            message = "Internal Server error";
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), message,
            request.getDescription(false));
        logger.error("error in controller" + getErrorMessage(request), ex);
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorMessage(WebRequest request) {

        StringBuilder errMsgbuilder = new StringBuilder(" \n\n");
        errMsgbuilder.append(" { \n\n")
            .append(" API Accessing = ")
            .append(((ServletWebRequest) request).getRequest().getRequestURI())
            .append(" } \n\n");

        return errMsgbuilder.toString();

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataException.class)
    public final ResponseEntity<Object> handleDataExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
            request.getDescription(false));
        logger.warn("invalid data Exception = " + getErrorMessage(request), ex);
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(x -> x.getDefaultMessage())
            .collect(Collectors.toList());

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errors.toString(),
            ex.getBindingResult().toString());
        logger.error("invalid data Exception = " + getErrorMessage(request), ex);
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}