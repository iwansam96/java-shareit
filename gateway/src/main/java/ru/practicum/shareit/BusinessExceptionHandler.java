package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {
    @ExceptionHandler({ StateIsIncorrectException.class })
    public ResponseEntity<Object> handleStateIsIncorrectException(StateIsIncorrectException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}