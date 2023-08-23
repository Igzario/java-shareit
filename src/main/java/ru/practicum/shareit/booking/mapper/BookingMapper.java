package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItemOwner;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking, UserDto userDto, ItemDto itemDto) {
        return new BookingDto(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                userDto,
                itemDto
        );
    }

    public Booking toDtoBooking(BookingDtoFromRequest bookingDtoFromRequest) {
        Booking booking = new Booking();
        booking.setStartDate(bookingDtoFromRequest.getStart());
        booking.setEndDate(bookingDtoFromRequest.getEnd());
        return booking;
    }

    public BookingDtoForItemOwner bookingDtoForItemOwner(Booking booking) {
        return new BookingDtoForItemOwner(
                booking.getId(),
                booking.getBookerId()
        );
    }
}