package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validated.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ItemDto {
    private Long id;

    @Pattern(regexp = ("(?i).*[a-zа-я\\d{10}].*"), groups = Update.class)
    @NotBlank(message = "Ошибка ввода - пустое поле Name", groups = Create.class)
    private String name;

    @Pattern(regexp = ("(?i).*[a-zа-я\\d{10}].*"), groups = Update.class)
    @NotBlank(message = "Ошибка ввода - пустое поле Description", groups = Create.class)
    private String description;

    @NotNull(message = "Ошибка ввода - Available: null", groups = Create.class)
    private Boolean available;

    private Long requestId;
}