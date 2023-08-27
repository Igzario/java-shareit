package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient service;

    private final String header = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> itemRequestDto(
            @Validated @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader(value = header) Long userId) {
        log.info("Запрос на добавление запроса от User'a c ID {}", userId);
        return service.save(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequests(
            @RequestHeader(value = header) Long userId) {
        log.info("Запрос на вывод запросов от пользователя с ID {}", userId);
        return service.getByRequestorId(userId);

    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequest(
            @RequestHeader(value = header) Long userId, @PathVariable Long requestId) {
        log.info("Запрос на вывод Бронирования с ID {} от пользователя с ID {}", requestId, userId);
        return service.get(requestId, userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllRequests(
            @RequestHeader(value = header) Long userId,
            @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false) @Positive Integer size) {
        log.info("Запрос на списка Бронирований от пользователя с ID {} - страницы {} - размер страницы {}", userId, from, size);
        return service.findRequestsOfOtherUsers(userId, from, size);

    }

}
