package com.watermelon.controller.exception;

import com.watermelon.model.response.EStatus;
import com.watermelon.model.response.ResponseData;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseData> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseData(null, EStatus.BAD_REQUEST.getStatus(), EStatus.BAD_REQUEST.getTitle(),"Invalid parameter type for 'id'. It should be of type long.")
        );
    }

    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
   public ResponseEntity<ResponseData> handleNotFoundException(NoHandlerFoundException ex){
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    			new ResponseData(null, EStatus.NOT_FOUND.getStatus(), EStatus.NOT_FOUND.getTitle(),ex.getMessage())
    			);
    }
    
    
    @ExceptionHandler(JwtValidationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleJwtValidationException(JwtValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ResponseData(null, EStatus.UNAUTHORIZED.getStatus(), EStatus.UNAUTHORIZED.getTitle(), ex.getMessage())
        );
    }

  
}
