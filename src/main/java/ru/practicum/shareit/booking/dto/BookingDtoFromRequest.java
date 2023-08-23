package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.validated.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@StartBeforeEndDateValid
public class BookingDtoFromRequest {
    @NonNull
    private final Long itemId;
    @NonNull
    @FutureOrPresent
    private final LocalDateTime start;
    @NonNull
    @Future
    private final LocalDateTime end;
}