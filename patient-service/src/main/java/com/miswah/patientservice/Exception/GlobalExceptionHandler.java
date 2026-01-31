package com.miswah.patientservice.Exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        errors.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        errors.put("message", "Error in request body");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(EmailAlreadyExistsException ex){
        log.error("Duplicate email {}", ex.getMessage());

        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        errors.put("message", "Error in request body");
        errors.put("errors", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePatientNotFoundException(PatientNotFoundException ex){
        log.error("patient not found {}", ex.getMessage());

        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.NOT_FOUND.value());
        errors.put("message", "Not Found");
        errors.put("errors", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }
}
