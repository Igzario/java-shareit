package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.*;

public interface ItemRepository {
    ItemDto getItemDto(Long id) throws ItemWithIdNotFound;

    Item getItem(Long id);

    void deleteItem(Long id);

    ItemDto addNewItem(ItemDto item, Long userId) throws ItemWithIdNotFound;

    ItemDto updateItem(ItemDto item, Long id, Long userId) throws ItemWithIdNotFound, UserWithIdNotFound, UserNotHaveThisItem;
}