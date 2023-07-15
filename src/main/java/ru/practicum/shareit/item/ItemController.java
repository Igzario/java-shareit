package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity addNewUser(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Запрос на добавление Item {} с userId {}", itemDto, userId);
        return itemService.addNewItem(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateItem(@RequestBody ItemDto itemDto, @PathVariable long id, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление Item с ID {}", id);
        return itemService.updateItem(itemDto, id, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity getItem(@PathVariable long id) {
        log.info("Запрос на вывод Item с ID {}", id);
        return itemService.getItem(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem(@PathVariable long id) {
        log.info("Запрос на удаление Item с ID {}", id);
        return itemService.deleteItem(id);
    }

    @GetMapping()
    public ResponseEntity getItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Запрос на вывод Items пользователя с ID {}", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity searchItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @RequestParam("text") String search) {
        log.info("Запрос на поиск Item по тексту: {}", search);
        return itemService.searchItem(userId, search);
    }
}