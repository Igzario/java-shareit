package ru.practicum.shareit.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Class entity, Long id) {
        super(entity.getSimpleName() + " с id " + id + " не найден");
    }
}