package ru.practicum.shareit.exception;

public class ItemStatusUnAvailableException extends Exception {
    public ItemStatusUnAvailableException(Long id) {
        super("Item с id " + id + " не доступен для бронирования");
    }
}
