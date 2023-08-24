package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validated.StartBeforeEndDateValid;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@StartBeforeEndDateValid
public class BookingDto {
    @Id
    private Long id;
    @NonNull
    @FutureOrPresent
    private LocalDateTime start;
    @NonNull
    @Future
    private LocalDateTime end;
    @Enumerated(EnumType.STRING)
    private Status status;
    private UserDto booker;
    private ItemDto item;
}