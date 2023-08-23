package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.exception.*;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDtoFromRequest bookingDtoFromRequest, Long userId)
            throws EntityNotFoundException, ItemStatusUnAvailableException, TimeBookingException, UserIsOwnerItemException;

    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved)
            throws EntityNotFoundException, UserNotHaveThisItemException, BookingAlwaysApprovedException;

    BookingDto getBooking(Long userId, Long bookingId)
            throws EntityNotFoundException, UserNotHaveThisItemException, BookingUserException;

    List<BookingDto> getAllBookingsForUser(Long userId, String state)
            throws EntityNotFoundException, UserNotHaveThisItemException, UnsupportedStatusException;

    List<BookingDto> getAllBookingItemsForUser(Long userId, String state)
            throws EntityNotFoundException, UserNotHaveThisItemException, UnsupportedStatusException;
}