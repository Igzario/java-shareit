package ru.practicum.shareit.exception;

public class EmailAlreadyExists extends Exception {
    public EmailAlreadyExists() {
        super("Пользователь с таким email уже существует");
    }
}