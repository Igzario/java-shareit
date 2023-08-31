package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookingServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    private BookingDtoFromRequest bookingDto;
    private UserDto userDto1;
    private UserDto userDtoBooker1;
    private BookingDto bookingDtoResponse;
    private BookingDto booking;
    ItemDto itemDtoCreate;

    @BeforeEach
    public void init() throws EmailAlreadyExists, EntityNotFoundException {
        UserDto userDto = new UserDto();
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        userDto1 = userService.addNewUser(userDto);

        UserDto userDtoBooker = new UserDto();
        userDtoBooker.setName("Mr. Booker");
        userDtoBooker.setEmail("unique@qwe.com");
        userDtoBooker1 = userService.addNewUser(userDtoBooker);

        itemDtoCreate = new ItemDto();
        itemDtoCreate.setId(null);
        itemDtoCreate.setName("Mr. Booker");
        itemDtoCreate.setDescription("Description");
        itemDtoCreate.setAvailable(true);
        itemDtoCreate.setRequestId(null);

        ItemDto itemDto1 = itemService.addNewItem(itemDtoCreate, userDto1.getId());
        bookingDto = new BookingDtoFromRequest(itemDto1.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.MAX);
    }

    @Test
    public void createBookingTest() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException {
        bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        test(bookingDtoResponse);
    }

    @Test
    public void createBookingTestException() {
        try {
            bookingDtoResponse = bookingService.addBooking(bookingDto, 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
        try {
            bookingDtoResponse = bookingService.addBooking(bookingDto, 1L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), UserIsOwnerItemException.class);
        }
        try {
            bookingDto.setEnd(LocalDateTime.now().minusHours(1));
            bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), TimeBookingException.class);
        }
    }

    @Test
    public void approveBookingTest() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException, BookingAlwaysApprovedException, UserNotHaveThisItemException {
        bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        booking = bookingService.approveBooking(userDto1.getId(), bookingDtoResponse.getId(), true);
        assertThat(booking.getStatus(), equalTo("APPROVED"));
    }

    @Test
    public void approveBookingTestException() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException, BookingAlwaysApprovedException, UserNotHaveThisItemException {
        bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        try {
            booking = bookingService.approveBooking(99L, bookingDtoResponse.getId(), true);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
        try {
            booking = bookingService.approveBooking(2L, bookingDtoResponse.getId(), true);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), UserNotHaveThisItemException.class);
        }
        try {
            itemDtoCreate.setAvailable(false);
            itemService.updateItem(itemDtoCreate, 1L, userDto1.getId());
            booking = bookingService.approveBooking(userDto1.getId(), bookingDtoResponse.getId(), true);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), ItemStatusUnAvailableException.class);
        }
    }

    @Test
    public void getBookingTest() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException, BookingUserException, UserNotHaveThisItemException {
        bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        BookingDto bookingDtoGet = bookingService.getBooking(userDto1.getId(), bookingDtoResponse.getId());
        test(bookingDtoGet);
    }

    @Test
    public void getBookingTestException() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException {
        bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        try {
            bookingService.getBooking(99L, bookingDtoResponse.getId());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), BookingUserException.class);
        }
        try {
            bookingService.getBooking(userDto1.getId(), 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    private void test(BookingDto bookingDtoResponse) {
        assertThat(bookingDto.getStart(), equalTo(bookingDtoResponse.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(bookingDtoResponse.getEnd()));
        assertThat(bookingDto.getItemId(), equalTo(bookingDtoResponse.getItem().getId()));
    }
}