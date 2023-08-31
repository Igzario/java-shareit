package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validated.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Ошибка ввода - пустое поле Name", groups = Create.class)
    private String name;

    @NotBlank(message = "Ошибка ввода - пустое поле Description", groups = Create.class)
    private String description;

    @NotNull(message = "Ошибка ввода - Available: null", groups = Create.class)
    private Boolean available;
    private Long requestId;
}