package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice(("ru.practicum.shareit"))
public class ErrorHandler {

    public ResponseEntity<String> constraint(ConstraintViolationException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity emailAlreadyExists(final EmailAlreadyExists exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }

    @ExceptionHandler
    public ResponseEntity userNotHaveThisItem(final UserNotHaveThisItemException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity entityNotFoundException(final EntityNotFoundException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity itemStatusUnAvailableException(final ItemStatusUnAvailableException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity timeBookingException(final TimeBookingException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity bookingUserException(final BookingUserException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity unsupportedStatusException(final UnsupportedStatusException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity bookingAlwaysApprovedException(final BookingAlwaysApprovedException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity userIsOwnerItemException(final UserIsOwnerItemException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity addCommentException(final AddCommentException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }
}