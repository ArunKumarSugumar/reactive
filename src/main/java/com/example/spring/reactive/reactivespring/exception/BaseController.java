package com.example.spring.reactive.reactivespring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author ArunKumar.Sugumar
 */
@Slf4j
@ControllerAdvice
public class BaseController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException runtimeException) {
        log.error("Exception caught in handleRuntimeException: {} ", runtimeException);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runtimeException.getMessage());
    }
}
