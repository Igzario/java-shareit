package ru.practicum.shareit.exception;

public class UserWithEmailAlreadyExists extends Exception {
    public UserWithEmailAlreadyExists() {
        super("Пользователь с таким email уже существует");
    }
}