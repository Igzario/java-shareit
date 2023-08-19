package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validated.*;
import ru.practicum.shareit.exception.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewUser(@Validated(Create.class) @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") Long userId) throws ItemNotFound {
        log.info("Запрос на добавление Item {} с userId {}", itemDto, userId);
        return itemService.addNewItem(itemDto, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@Validated(Update.class) @RequestBody ItemDto itemDto, @PathVariable long id,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId)
            throws UserWithIdNotFound, ItemNotFound, UserNotHaveThisItem {
        log.info("Запрос на обновление Item с ID {}", id);
        return itemService.updateItem(itemDto, id, userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable long id) throws ItemNotFound {
        log.info("Запрос на вывод Item с ID {}", id);
        return itemService.getItemDto(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable long id) {
        log.info("Запрос на удаление Item с ID {}", id);
        itemService.deleteItem(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Запрос на вывод Items пользователя с ID {}", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @RequestParam("text") String search) {
        log.info("Запрос на поиск Item по тексту: {}", search);
        return itemService.searchItem(userId, search);
    }
}