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
        BookingDto bookingDto = new BookingDto(booking.getStartDate(), booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        return bookingDto;
    }

    public Booking toDtoBooking(BookingDtoFromRequest bookingDtoFromRequest) {
        Booking booking = new Booking();
        booking.setStartDate(bookingDtoFromRequest.getStart());
        booking.setEndDate(bookingDtoFromRequest.getEnd());
        return booking;
    }

    public BookingDtoForItemOwner bookingDtoForItemOwner(Booking booking) {
        BookingDtoForItemOwner bookingDtoForItemOwner = new BookingDtoForItemOwner();
        bookingDtoForItemOwner.setBookerId(booking.getBookerId());
        bookingDtoForItemOwner.setId(booking.getId());
        return bookingDtoForItemOwner;
    }
}