package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String HEADER_REQUEST = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto itemRequestDto(
            @Validated @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader(value = HEADER_REQUEST) Long userId)
            throws EntityNotFoundException {
        log.info("Запрос на добавление запроса от User'a c ID {}", userId);
        return itemRequestService.addItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getRequests(
            @RequestHeader(value = HEADER_REQUEST) Long userId)
            throws EntityNotFoundException {
        log.info("Запрос на вывод запросов от пользователя с ID {}", userId);
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getRequest(
            @RequestHeader(value = HEADER_REQUEST) Long userId, @PathVariable Long requestId)
            throws EntityNotFoundException {
        log.info("Запрос на вывод Бронирования с ID {} от пользователя с ID {}", requestId, userId);
        return itemRequestService.getItemRequest(requestId, userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getAllRequests(
            @RequestHeader(value = HEADER_REQUEST) Long userId,
            @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false) @Positive Integer size)
            throws EntityNotFoundException {
        log.info("Запрос на списка Бронирований от пользователя с ID {} - страницы {} - размер страницы {}", userId, from, size);
        return itemRequestService.getItemRequests(userId, from, size);
    }

}
