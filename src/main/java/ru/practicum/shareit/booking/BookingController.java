package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.exception.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(
            @Validated @RequestBody BookingDtoFromRequest bookingDtoFromRequest,
            @RequestHeader(value = header) Long userId)
            throws EntityNotFoundException, ItemStatusUnAvailableException, TimeBookingException, UserIsOwnerItemException {
        log.info("Запрос на добавление бронирования Item'a с ID {} от User'a c ID {}", bookingDtoFromRequest.getItemId(), userId);
        return bookingService.addBooking(bookingDtoFromRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(
            @RequestHeader(value = header) Long userId, @PathVariable Long bookingId,
            @RequestParam("approved") Boolean approved)
            throws EntityNotFoundException, UserNotHaveThisItemException, BookingAlwaysApprovedException {
        log.info("Запрос на подтверждение/отколнение бронирования с ID {} от User'a c ID {}. Approved: {}", bookingId, userId, approved);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(
            @RequestHeader(value = header) Long userId, @PathVariable Long bookingId)
            throws EntityNotFoundException, UserNotHaveThisItemException, BookingUserException {
        log.info("Запрос на вывод Бронирования с ID {} от пользователя с ID {}", bookingId, userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsForUser(
            @RequestHeader(value = header) Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
            @Validated @Positive @RequestParam(value = "size", required = false) Integer size)
            throws EntityNotFoundException, UserNotHaveThisItemException, UnsupportedStatusException {
        log.info("Запрос на вывод всех Бронирований от пользователя с ID {}", userId);
        return bookingService.getAllBookingsForUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsForUserOwner(
            @RequestHeader(value = header) Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false) @Positive Integer size)
            throws EntityNotFoundException, UserNotHaveThisItemException, UnsupportedStatusException {
        log.info("Запрос на вывод всех Бронирований от пользователя с ID {}", userId);
        return bookingService.getAllBookingItemsForUser(userId, state, from, size);
    }
}