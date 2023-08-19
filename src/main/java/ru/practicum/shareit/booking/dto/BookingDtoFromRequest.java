package ru.practicum.shareit.booking.dto;
import lombok.Data;
import lombok.NonNull;
import java.time.LocalDateTime;

@Data
public class BookingDtoFromRequest {
    @NonNull
    private final Long itemId;
    @NonNull
    private final LocalDateTime start;
    @NonNull
    private final LocalDateTime end;
}