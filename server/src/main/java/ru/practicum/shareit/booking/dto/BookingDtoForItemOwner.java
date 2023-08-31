package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.persistence.Id;

@Data
public class BookingDtoForItemOwner {
    @Id
    private Long id;
    private Long bookerId;
}