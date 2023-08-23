package ru.practicum.shareit.exception;

public class UnsupportedStatusException extends Exception {
    public UnsupportedStatusException() {
        super("Unknown state: UNSUPPORTED_STATUS");
    }

}
