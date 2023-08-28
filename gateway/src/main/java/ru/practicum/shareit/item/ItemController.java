package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final String HEADER_REQUEST = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                             @RequestHeader(value = HEADER_REQUEST) Long userId) {
        log.info("Запрос на добавление Item {} с userId {}", itemDto, userId);
        return itemClient.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Validated(Update.class) @RequestBody ItemDto itemDto,
                                             @PathVariable long itemId, @RequestHeader(value = HEADER_REQUEST) Long userId) {
        log.info("Запрос на обновление Item с ID {}", itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(value = HEADER_REQUEST) Long userId) {
        log.info("Запрос на вывод Item с ID {} от пользователя с ID {}", itemId, userId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItems(@RequestHeader(value = HEADER_REQUEST) long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "3") int size) {
        log.info("Запрос на вывод Items пользователя с ID {}", userId);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(value = HEADER_REQUEST) Long userId,
                                              @RequestParam(defaultValue = "") String text,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "3") int size) {

        log.info("Запрос на поиск Item по тексту: {}", text);
        return itemClient.searchItems(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(value = HEADER_REQUEST) Long userId,
                                             @Validated(Create.class) @RequestBody CommentDto commentDto,
                                             @PathVariable Long itemId) {
        log.info("Запрос на добавление комментария к Item с ID {} от пользователя с ID {}", itemId, userId);
        return itemClient.addComment(itemId, userId, commentDto);
    }
}