package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
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
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> save(@RequestHeader(value = header) long userId,
                                       @RequestBody @Validated BookItemRequestDto requestDto)  {
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> setStatus(@PathVariable long bookingId,
                                            @RequestHeader(value = header) long userId,
                                            @RequestParam boolean approved) {

        return bookingClient.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@RequestHeader(value = header) long userId,
                                           @PathVariable long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookings(@RequestHeader(value = header) long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                              @Validated @Positive @RequestParam(value = "size", required = false) Integer size) throws UnsupportedStatusException {
        var bookingState = BookingState.from(state)
                .orElseThrow(UnsupportedStatusException::new);
        return bookingClient.getBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(value = header) long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(required = false) Integer from,
                                                   @Positive @RequestParam(required = false) Integer size) throws UnsupportedStatusException {
        var bookingState = BookingState.from(state)
                .orElseThrow(UnsupportedStatusException::new);
        return bookingClient.getOwnerBookings(userId, bookingState, from, size);
    }

}