package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validated.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    private Long id;

    @Pattern(regexp = ("(?i).*[a-zа-я].*"), groups = Update.class)
    @NotBlank(message = "Ошибка ввода - пустое поле Name", groups = Create.class)
    private String name;

    @Email(message = "Ошибка ввода - Email: not email format", groups = {Create.class, Update.class})
    @NotBlank(message = "Ошибка ввода - пустое поле email", groups = Create.class)
    private String email;
}