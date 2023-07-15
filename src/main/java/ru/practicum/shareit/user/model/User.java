package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private Long id;
    private String name;
    @NotNull(message = "Ошибка ввода - Email: null")
    @NotEmpty(message = "Ошибка ввода - Email: empty")
    @Email(message = "Ошибка ввода - Email: not email format")
    private String email;
}