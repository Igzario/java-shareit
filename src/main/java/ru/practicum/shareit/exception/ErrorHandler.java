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

//    @ExceptionHandler
//    public ResponseEntity userWithIdNotFound(final UserWithIdNotFound exception) {
//        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
//        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
//    }
//
//    @ExceptionHandler
//    public ResponseEntity itemNotFound(final ItemNotFound exception) {
//        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
//        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
//    }

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
    public ResponseEntity ItemStatusUnAvailableException(final ItemStatusUnAvailableException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity TimeBookingException(final TimeBookingException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity BookingUserException(final BookingUserException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity UnsupportedStatusException(final UnsupportedStatusException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity BookingAlwaysApprovedException(final BookingAlwaysApprovedException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }

    @ExceptionHandler
    public ResponseEntity UserIsOwnerItemException(final UserIsOwnerItemException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(404));
    }

    @ExceptionHandler
    public ResponseEntity AddCommentException(final AddCommentException exception) {
        log.error(HttpStatus.valueOf(400) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.valueOf(400));
    }
}