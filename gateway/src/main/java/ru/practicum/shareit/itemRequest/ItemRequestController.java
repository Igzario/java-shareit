package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient service;

    private static final String HEADER_REQUEST = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Validated @RequestBody ItemRequestDto itemRequestDto,
                                                 @RequestHeader(value = HEADER_REQUEST) Long userId) {
        log.info("Запрос на добавление запроса от User'a c ID {}", userId);
        return service.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getByRequestorId(@RequestHeader(value = HEADER_REQUEST) Long userId) {
        log.info("Запрос на вывод запросов от пользователя с ID {}", userId);
        return service.getByRequestorId(userId);

    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(value = HEADER_REQUEST) Long userId,
                                             @PathVariable Long requestId) {
        log.info("Запрос на вывод Бронирования с ID {} от пользователя с ID {}", requestId, userId);
        return service.getRequest(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findRequestsOfOtherUsers(@RequestHeader(value = HEADER_REQUEST) Long userId,
                                                 @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                 @RequestParam(value = "size", required = false) @Positive Integer size) {
        log.info("Запрос на списка Бронирований от пользователя с ID {} - страницы {} - размер страницы {}", userId, from, size);
        return service.findRequestsOfOtherUsers(userId, from, size);

    }

}
