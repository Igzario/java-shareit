package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.*;

import java.util.List;

public interface ItemService {

    ItemDto getItemDto(Long id) throws ItemWithIdNotFound;

    List<ItemDto> getItemsByUser(Long userId);

    void deleteItem(Long id);

    ItemDto addNewItem(ItemDto item, Long userId) throws ItemWithIdNotFound;

    ItemDto updateItem(ItemDto item, Long id, Long userId) throws UserWithIdNotFound, ItemWithIdNotFound, UserNotHaveThisItem;

    List<ItemDto> searchItem(Long userId, String search);
}