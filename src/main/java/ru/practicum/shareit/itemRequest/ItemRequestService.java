package ru.practicum.shareit.itemRequest;

import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId) throws EntityNotFoundException;

    List<ItemRequestDto> getRequests(Long userId) throws EntityNotFoundException;

    ItemRequestDto getItemRequest(Long requestId, Long userId) throws EntityNotFoundException;

    List<ItemRequestDto> getItemRequests(Long userId, Integer from, Integer size) throws EntityNotFoundException;
}