package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private final Long id;
    @NotNull(message = "Ошибка ввода - Name: null")
    @NotEmpty(message = "Ошибка ввода - Name: empty")
    private final String name;
    @NotNull(message = "Ошибка ввода - Description: null")
    @NotEmpty(message = "Ошибка ввода - Description: empty")
    private final String description;
    @NotNull(message = "Ошибка ввода - Available: null")
    private final Boolean available;
    private final Long requestId;
}