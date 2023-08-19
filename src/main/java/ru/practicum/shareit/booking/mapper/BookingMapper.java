package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItemOwner;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking, UserDto userDto, ItemDto itemDto) {
        return new BookingDto(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                userDto,
                itemDto
        );
    }

    public static Booking toDtoBooking(BookingDtoFromRequest bookingDtoFromRequest) {
        Booking newBooking = new Booking();
        newBooking.setStartDate(bookingDtoFromRequest.getStart());
        newBooking.setEndDate(bookingDtoFromRequest.getEnd());
        return newBooking;
    }

    public static BookingDtoForItemOwner BookingDtoForItemOwner(Booking booking) {
        return new BookingDtoForItemOwner(
                booking.getId(),
                booking.getBookerId()
        );
    }
}