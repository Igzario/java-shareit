package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    @Id
    private final Long id;
    @NonNull
    private final LocalDateTime start;
    @NonNull
    private final LocalDateTime end;
    @Enumerated(EnumType.STRING)
    private final Status status;
    private final UserDto booker;
    private final ItemDto item;
}