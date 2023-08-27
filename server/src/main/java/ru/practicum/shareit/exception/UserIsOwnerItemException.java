package ru.practicum.shareit.exception;

public class UserIsOwnerItemException extends Exception {
    public UserIsOwnerItemException(Long userId, Long itemID) {
        super("User с ID " + userId + " является владельцем вещи с ID " + itemID);
    }
}
