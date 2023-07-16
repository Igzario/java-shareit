package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.*;

public interface ItemRepository {
    ItemDto getItemDto(Long id) throws ItemNotFound;

    Item getItem(Long id);

    void deleteItem(Long id);

    ItemDto addNewItem(ItemDto item, Long userId) throws ItemNotFound;

    ItemDto updateItem(ItemDto item, Long id, Long userId) throws ItemNotFound, UserWithIdNotFound, UserNotHaveThisItem;
}