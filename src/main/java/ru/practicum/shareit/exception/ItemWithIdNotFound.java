package ru.practicum.shareit.exception;

public class ItemWithIdNotFound extends Exception {
    public ItemWithIdNotFound() {
        super("Item с таким id не существует");
    }
}