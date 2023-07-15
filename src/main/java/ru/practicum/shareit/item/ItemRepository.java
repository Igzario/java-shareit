package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    ResponseEntity getItemDto(Long id);

    Item getItem(Long id);

    ResponseEntity deleteItem(Long id);

    ResponseEntity addNewItem(ItemDto item, Long userId);

    ResponseEntity updateItem(ItemDto item, Long id, Long userId);
}