package ru.practicum.shareit.exception;

public class UserWithIdNotFound extends Exception {
    public UserWithIdNotFound() {
        super("Пользователь с таким id не существует");
    }
}