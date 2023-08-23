package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.validated.*;
import ru.practicum.shareit.exception.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewUser(@Validated(Create.class) @RequestBody ItemDto itemDto,
                              @RequestHeader(value = header) Long userId) throws EntityNotFoundException {
        log.info("Запрос на добавление Item {} с userId {}", itemDto, userId);
        return itemService.addNewItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@Validated(Update.class) @RequestBody ItemDto itemDto, @PathVariable long itemId,
                              @RequestHeader(value = header) Long userId)
            throws UserNotHaveThisItemException, EntityNotFoundException {
        log.info("Запрос на обновление Item с ID {}", itemId);
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Object getItem(@PathVariable long itemId,
                          @RequestHeader(value = header) Long userId) throws EntityNotFoundException {
        log.info("Запрос на вывод Item с ID {} от пользователя с ID {}", itemId, userId);
        return itemService.getItemDto(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable long itemId) {
        log.info("Запрос на удаление Item с ID {}", itemId);
        itemService.deleteItem(itemId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoForOwner> getItemsByUser(@RequestHeader(value = header) Long userId) {
        log.info("Запрос на вывод Items пользователя с ID {}", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItem(@RequestHeader(value = header) Long userId,
                                    @RequestParam("text") String search) {
        log.info("Запрос на поиск Item по тексту: {}", search);
        return itemService.searchItem(userId, search);
    }


    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(@RequestHeader(value = header) Long userId,
                                 @Validated(Create.class) @RequestBody CommentDto commentDto,
                                 @PathVariable Long itemId) throws EntityNotFoundException, AddCommentException {
        log.info("Запрос на добавление комментария к Item с ID {} от пользователя с ID {}", itemId, userId);
        return itemService.addComment(userId, itemId, commentDto);
    }
}