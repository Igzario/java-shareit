package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingDto addBooking(BookingDtoFromRequest bookingDtoFromRequest, Long userId)
            throws EntityNotFoundException, ItemStatusUnAvailableException, TimeBookingException, UserIsOwnerItemException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        Item item = itemRepository.findById(bookingDtoFromRequest.getItemId()).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - Item");
            return new EntityNotFoundException(Item.class, bookingDtoFromRequest.getItemId());
        });
        if (item.getOwner().equals(userId)) {
            log.info("Сгенерирован UserIsOwnerItemException");
            throw new UserIsOwnerItemException(userId, item.getId());
        }
        Booking booking;
        if (item.getAvailable()) {
            booking = BookingMapper.toDtoBooking(bookingDtoFromRequest);
            booking.setStatus(Status.WAITING);
            booking.setItemId(item.getId());
            booking.setBookerId(userId);
            bookingRepository.save(booking);
        } else {
            log.info("Сгенерирован ItemStatusUnAvailableException");
            throw new ItemStatusUnAvailableException(item.getId());
        }
        log.info("Создано бронирование: {}", booking);
        return BookingMapper.toBookingDto(booking, UserMapper.toUserDto(user), ItemMapper.toItemDto(item));
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved)
            throws EntityNotFoundException, UserNotHaveThisItemException, BookingAlwaysApprovedException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - Booking");
            return new EntityNotFoundException(Booking.class, userId);
        });
        User user = userRepository.findById(booking.getBookerId()).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - Item");
            return new UserNotHaveThisItemException(userId, booking.getItemId());
        });
        if (item.getOwner().equals(userId)) {
            if (approved) {
                if (booking.getStatus() != Status.APPROVED) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    log.info("Сгенерирован BookingAlwaysApprovedException");
                    throw new BookingAlwaysApprovedException(bookingId);
                }
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            log.info("Сгенерирован UserNotHaveThisItemException");
            throw new UserNotHaveThisItemException(userId, item.getId());
        }
        log.info("Бронирование подтверждено хозяином вещи: {}", booking);
        return BookingMapper.toBookingDto(booking, UserMapper.toUserDto(user), ItemMapper.toItemDto(item));
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBooking(Long userId, Long bookingId)
            throws EntityNotFoundException, UserNotHaveThisItemException, BookingUserException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - Booking");
            return new EntityNotFoundException(Booking.class, userId);
        });
        User user = userRepository.findById(booking.getBookerId()).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - Item");
            return new UserNotHaveThisItemException(userId, booking.getItemId());
        });
        if (userId.equals(item.getOwner()) || userId.equals(booking.getBookerId())) {
            log.info("Возвращено бронирование: {}", booking);
            return BookingMapper.toBookingDto(booking, UserMapper.toUserDto(user), ItemMapper.toItemDto(item));
        } else {
            log.info("Сгенерирован BookingUserException");
            throw new BookingUserException(userId, bookingId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingsForUser(Long userId, String state, Integer from, Integer size)
            throws EntityNotFoundException, UserNotHaveThisItemException, UnsupportedStatusException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        List<Booking> bookingList = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                bookingList = bookingRepository.findBookingsByBookerIdOrderByStartDateDesc(userId);
                break;
            case ("FUTURE"):
                bookingList = bookingRepository.findBookingsByBookerIdAndStartDateAfterOrderByStartDateDesc(userId, LocalDateTime.now().minusSeconds(5));
                break;
            case ("WAITING"):
                bookingList = bookingRepository.findBookingsByBookerIdAndStatus(userId, Status.WAITING);
                break;
            case ("REJECTED"):
                bookingList = bookingRepository.findBookingsByBookerIdAndStatus(userId, Status.REJECTED);
                break;
            case ("CURRENT"):
                bookingList = bookingRepository.findBookingsByBookerIdAndStartDateBeforeAndEndDateAfter(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case ("PAST"):
                bookingList = bookingRepository.findBookingsByBookerIdAndEndDateBeforeOrderByStartDateDesc(userId, LocalDateTime.now());
                break;
            default:
                log.info("Сгенерирован UnsupportedStatusException");
                throw new UnsupportedStatusException();
        }
        ArrayList<BookingDto> bookingDtoList = new ArrayList<>();
        User userForConvert;
        Item itemForConvert;
        if (size != null && from != null) {
            Pageable pageable = PageRequest.of(from, size, Sort.by("startDate"));
            bookingList = bookingRepository.findBookingsByBookerId(userId , pageable).getContent();
        }
        for (Booking booking : bookingList) {
            userForConvert = userRepository.findById(booking.getBookerId()).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
            itemForConvert = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new UserNotHaveThisItemException(userId, booking.getItemId()));
            bookingDtoList.add(BookingMapper.toBookingDto(booking, UserMapper.toUserDto(userForConvert), ItemMapper.toItemDto(itemForConvert)));
        }
        log.info("Возвращен список бронирований: {}", bookingDtoList);
        return bookingDtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingItemsForUser(Long userId, String state, Integer from, Integer size)
            throws EntityNotFoundException, UserNotHaveThisItemException, UnsupportedStatusException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        List<Long> bookings = new ArrayList<>();
        List<BookingDto> bookingDtoList = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                bookings = bookingRepository.getAllBookingItemsForUser(userId);
                break;
            case ("FUTURE"):
                bookings = bookingRepository.getAllBookingItemsForUserFuture(userId, LocalDateTime.now().minusSeconds(4));
                break;
            case ("WAITING"):
                bookings = bookingRepository.getAllBookingItemsForUserStatus(userId, Status.WAITING);
                break;
            case ("REJECTED"):
                bookings = bookingRepository.getAllBookingItemsForUserStatus(userId, Status.REJECTED);
                break;
            case ("CURRENT"):
                bookings = bookingRepository.findBookingsCurrent(userId, LocalDateTime.now());
                break;
            case ("PAST"):
                bookings = bookingRepository.findBookingsPast(userId, LocalDateTime.now());
                break;
            default:
                log.info("Сгенерирован UnsupportedStatusException");
                throw new UnsupportedStatusException();
        }
        if (size != null && from != null) {
            bookings = bookingRepository.getAllBookingItemsForUserId(userId , PageRequest.of(from, size)).getContent();
        }
        for (Long id : bookings) {
            Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Booking.class, userId));
            User userForConvert = userRepository.findById(booking.getBookerId()).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
            Item itemForConvert = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new UserNotHaveThisItemException(userId, booking.getItemId()));
            bookingDtoList.add(BookingMapper.toBookingDto(booking, UserMapper.toUserDto(userForConvert), ItemMapper.toItemDto(itemForConvert)));
        }
        log.info("Возвращен список бронирований: {}", bookingDtoList);
        return bookingDtoList;
    }
}