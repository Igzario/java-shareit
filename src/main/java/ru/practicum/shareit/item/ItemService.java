package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

    ResponseEntity getItem(Long id);

    ResponseEntity getItemsByUser(Long userId);

    ResponseEntity deleteItem(Long id);

    ResponseEntity addNewItem(ItemDto item, Long userId);

    ResponseEntity updateItem(ItemDto item, Long id, Long userId);

    ResponseEntity searchItem(Long userId, String search);
}
