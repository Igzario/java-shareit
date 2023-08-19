package ru.practicum.shareit.exception;

public class ItemNotFound extends Exception {
    public ItemNotFound() {
        super("Item с таким id не существует");
    }
}