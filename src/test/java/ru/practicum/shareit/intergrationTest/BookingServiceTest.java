package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        ItemDto itemDtoCreate = new ItemDto();
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
        BookingDto bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        test(bookingDtoResponse);
    }

    @Test
    public void patchBookingTest() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException, BookingAlwaysApprovedException, UserNotHaveThisItemException {
        BookingDto bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        BookingDto booking = bookingService.approveBooking(userDto1.getId(), bookingDtoResponse.getId(), true);
        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    public void getBookingTest() throws ItemStatusUnAvailableException, UserIsOwnerItemException, TimeBookingException, EntityNotFoundException, BookingUserException, UserNotHaveThisItemException {
        BookingDto bookingDtoResponse = bookingService.addBooking(bookingDto, userDtoBooker1.getId());
        BookingDto bookingDtoGet = bookingService.getBooking(userDto1.getId(), bookingDtoResponse.getId());
        test(bookingDtoGet);
    }

    private void test(BookingDto bookingDtoResponse) {
        assertThat(bookingDto.getStart(), equalTo(bookingDtoResponse.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(bookingDtoResponse.getEnd()));
        assertThat(bookingDto.getItemId(), equalTo(bookingDtoResponse.getItem().getId()));
    }
}