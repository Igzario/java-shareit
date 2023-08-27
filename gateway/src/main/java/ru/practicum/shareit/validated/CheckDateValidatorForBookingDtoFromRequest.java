package ru.practicum.shareit.validated;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDateValidatorForBookingDtoFromRequest
        implements ConstraintValidator<StartBeforeEndDateValid, BookItemRequestDto> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(
            BookItemRequestDto bookingDtoFromRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDtoFromRequest.getStart();
        LocalDateTime end = bookingDtoFromRequest.getEnd();
        if (start == end || start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}