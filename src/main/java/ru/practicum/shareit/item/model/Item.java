package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Item {
    private Long id;
    @NotNull(message = "Ошибка ввода - Name: null")
    @NotEmpty(message = "Ошибка ввода - Name: empty")
    private String name;
    @NotNull(message = "Ошибка ввода - Description: null")
    @NotEmpty(message = "Ошибка ввода - Description: empty")
    private String description;
    @NotNull(message = "Ошибка ввода - Available: null")
    @NotEmpty(message = "Ошибка ввода - Available: empty")
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public boolean isAvailable() {
        return this.available;
    }
}