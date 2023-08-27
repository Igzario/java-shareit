package ru.practicum.shareit.exception;

public class BookingUserException extends Exception {
    public BookingUserException(Long userId, Long bookingId) {
        super("User с id " + userId + " не имеет отношения к бронированию с ID " + bookingId);
    }
}