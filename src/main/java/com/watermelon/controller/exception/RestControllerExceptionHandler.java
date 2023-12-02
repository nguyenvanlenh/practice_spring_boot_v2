package com.watermelon.controller.exception;

import com.watermelon.model.response.EStatus;
import com.watermelon.model.response.ResponseObject;

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
    public ResponseEntity<ResponseObject> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject(null, EStatus.BAD_REQUEST.getStatus(), EStatus.BAD_REQUEST.getTitle(),"Invalid parameter type for 'id'. It should be of type long.")
        );
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
   public ResponseEntity<ResponseObject> handleNotFoundException(NoHandlerFoundException ex){
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    			new ResponseObject(null, EStatus.NOT_FOUND.getStatus(), EStatus.NOT_FOUND.getTitle(),ex.getMessage())
    			);
    }
    
    
    @ExceptionHandler(JwtValidationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleJwtValidationException(JwtValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ResponseObject(null, EStatus.UNAUTHORIZED.getStatus(), EStatus.UNAUTHORIZED.getTitle(), ex.getMessage())
        );
    }
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResponseEntity<ResponseObject> handleAccessDeniedException(AccessDeniedException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
//                new ResponseObject(null, EStatus.FORBIDDEN.getStatus(), "Access denied: " + ex.getMessage())
//        );
//    }
    
  
}
