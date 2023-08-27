package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                             @RequestHeader(value = header) Long userId) {
        log.info("Запрос на добавление Item {} с userId {}", itemDto, userId);
        return itemClient.save(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(
            @Validated(Update.class) @RequestBody ItemDto itemDto,
            @PathVariable long itemId, @RequestHeader(value = header) Long userId) {
        log.info("Запрос на обновление Item с ID {}", itemId);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(value = header) Long userId) {
        log.info("Запрос на вывод Item с ID {} от пользователя с ID {}", itemId, userId);
        return itemClient.get(itemId, userId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItems(
            @RequestHeader(value = header) long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "3") int size) {
        log.info("Запрос на вывод Items пользователя с ID {}", userId);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(
            @RequestHeader(value = header) Long userId, @RequestParam(defaultValue = "", required = false) String text,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
            @Positive @RequestParam(required = false, defaultValue = "3") int size) {

        log.info("Запрос на поиск Item по тексту: {}", text);
        return itemClient.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addComment(@RequestHeader(value = header) Long userId,
                                             @Validated(Create.class) @RequestBody CommentDto commentDto,
                                             @PathVariable Long itemId) {
        log.info("Запрос на добавление комментария к Item с ID {} от пользователя с ID {}", itemId, userId);
        return itemClient.saveComment(itemId, userId, commentDto);
    }
}