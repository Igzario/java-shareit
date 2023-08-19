package ru.practicum.shareit.exception;

public class BookingAlwaysApprovedException extends Exception {
    public BookingAlwaysApprovedException(Long bookingId) {
        super("Бронирование с ID " + bookingId + " уже подтверждено");
    }
}
