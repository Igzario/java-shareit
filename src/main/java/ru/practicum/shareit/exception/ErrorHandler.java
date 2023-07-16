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
    public ResponseEntity emailAlreadyExists(final EmailAlreadyExists exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }

    @ExceptionHandler
    public ResponseEntity userWithIdNotFound(final UserWithIdNotFound exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity itemNotFound(final ItemNotFound exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity userNotHaveThisItem(final UserNotHaveThisItem exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }
}