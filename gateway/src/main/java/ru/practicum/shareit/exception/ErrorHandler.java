package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice(("ru.practicum.shareit"))
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity unsupportedStatusException(final UnsupportedStatusException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }
}