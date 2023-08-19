package ru.practicum.shareit.exception;

public class AddCommentException extends Exception{
    public AddCommentException() {
        super("Ошибка добавления комментария");
    }
}