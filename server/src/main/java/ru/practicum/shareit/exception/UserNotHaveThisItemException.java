package ru.practicum.shareit.exception;

public class UserNotHaveThisItemException extends Exception {
    public UserNotHaveThisItemException(Long userId, Long itemId) {
        super("Пользователь с id " + userId + " не является владельцем вещи с id " + itemId);
    }
}