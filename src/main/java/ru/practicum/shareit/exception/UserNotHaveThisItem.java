package ru.practicum.shareit.exception;

public class UserNotHaveThisItem extends Exception {
    public UserNotHaveThisItem(Long userId, Long itemId) {
        super("Пользователь с id " + userId + " не является владельцем вещи с id " + itemId);
    }
}