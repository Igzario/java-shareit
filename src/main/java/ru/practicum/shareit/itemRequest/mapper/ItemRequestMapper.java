package ru.practicum.shareit.itemRequest.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.time.LocalDateTime;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public ItemRequest itemRequest(ItemRequestDto itemRequestDto, Long requestorId, LocalDateTime start) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestorId(requestorId);
        itemRequest.setCreated(start);
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }
}