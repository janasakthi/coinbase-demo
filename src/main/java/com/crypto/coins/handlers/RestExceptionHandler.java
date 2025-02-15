package com.crypto.coins.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.crypto.coins.dto.ErrorResponse;
import com.crypto.coins.exception.UnsupportedDigitalCoin;
import com.crypto.coins.exception.ValidationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(UnsupportedDigitalCoin.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
    		UnsupportedDigitalCoin ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
    		ValidationException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	

}
