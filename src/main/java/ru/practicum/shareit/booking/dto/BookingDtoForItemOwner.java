package ru.practicum.shareit.booking.dto;
import lombok.Data;
import javax.persistence.Id;

@Data
public class BookingDtoForItemOwner {
    @Id
    private final Long id;
    private final Long bookerId;
}