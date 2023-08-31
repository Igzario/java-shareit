package ru.practicum.shareit.validated;

import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDateValidatorForBookingDtoFromRequest implements ConstraintValidator<StartBeforeEndDateValid, BookingDtoFromRequest> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDtoFromRequest bookingDtoFromRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDtoFromRequest.getStart();
        LocalDateTime end = bookingDtoFromRequest.getEnd();
        if (start == end) {
            return false;
        }
        return start.isBefore(end);
    }
}