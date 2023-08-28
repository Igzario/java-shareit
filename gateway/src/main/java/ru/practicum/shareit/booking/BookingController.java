package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private final String HEADER_REQUEST = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(value = HEADER_REQUEST) long userId,
                                       @RequestBody @Validated BookItemRequestDto requestDto) {
        log.info("Запрос на добавление бронирования Item'a с ID {} от User'a c ID {}", requestDto.getItemId(), userId);
        return bookingClient.addBooking(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setStatus(@PathVariable long bookingId,
                                            @RequestHeader(value = HEADER_REQUEST) long userId,
                                            @RequestParam boolean approved) {
        log.info("Запрос на подтверждение/отколнение бронирования с ID {} от User'a c ID {}. Approved: {}", bookingId, userId, approved);
        return bookingClient.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@RequestHeader(value = HEADER_REQUEST) long userId,
                                           @PathVariable long bookingId) {
        log.info("Запрос на вывод Бронирования с ID {} от пользователя с ID {}", bookingId, userId);
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(value = HEADER_REQUEST) long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                              @Validated @Positive @RequestParam(value = "size", required = false) Integer size) throws UnsupportedStatusException {
        log.info("Запрос на вывод всех Бронирований от пользователя с ID {}", userId);
        var bookingState = BookingState.from(state)
                .orElseThrow(UnsupportedStatusException::new);
        return bookingClient.getBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(value = HEADER_REQUEST) long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(required = false) Integer from,
                                                   @Positive @RequestParam(required = false) Integer size) throws UnsupportedStatusException {
        log.info("Запрос на вывод всех Бронирований от пользователя с ID {}", userId);
        var bookingState = BookingState.from(state)
                .orElseThrow(UnsupportedStatusException::new);
        return bookingClient.getOwnerBookings(userId, bookingState, from, size);
    }

}