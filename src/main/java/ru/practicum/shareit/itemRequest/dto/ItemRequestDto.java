package ru.practicum.shareit.itemRequest.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Ошибка ввода - пустое поле Description")
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}