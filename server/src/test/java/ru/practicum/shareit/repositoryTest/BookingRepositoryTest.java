package ru.practicum.shareit.repositoryTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    Item item;
    Item item2;
    User user;
    User user2;

    @BeforeEach
    void addUserAndItem() {
        user = new User();
        user.setName("Oleg");
        user.setEmail("ol@mail.com");
        em.persist(user);

        user2 = new User();
        user2.setName("Alex");
        user2.setEmail("al@mail.com");
        em.persist(user2);

        item = new Item();
        item.setName("item1");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(1L);
        em.persist(item);

        item2 = new Item();

        item2.setName("item2");
        item2.setDescription("desc2");
        item2.setAvailable(true);
        item2.setOwner(2L);
        em.persist(item2);
    }

    @Test
    void testRepository() throws EntityNotFoundException {
        EntityManager em1 = em.getEntityManager();
        Booking booking = new Booking();
        booking.setItemId(1L);
        booking.setBookerId(1L);
        booking.setStartDate(LocalDateTime.now());
        booking.setEndDate(LocalDateTime.now().plusHours(1));
        booking.setStatus(Status.WAITING);
        em.persist(booking);

        Item itemSearch1 = itemRepository.findById(1L).orElseThrow(() -> {
            return new EntityNotFoundException(Item.class, 1L);
        });
        Assertions.assertEquals(1L, itemSearch1.getId());
        Assertions.assertEquals("item1", itemSearch1.getName());

        User userSearch1 = userRepository.findById(2L).orElseThrow(() -> {
            return new EntityNotFoundException(User.class, 2L);
        });

        Assertions.assertEquals(2L, userSearch1.getId());
        Assertions.assertEquals("Alex", userSearch1.getName());

        em1.createNativeQuery("INSERT INTO BOOKINGS (START_DATE, END_DATE, ITEM_ID, BOOKER_ID, STATUS)" +
                        "VALUES (?,?,?,?,?)")
                .setParameter(1, LocalDateTime.now())
                .setParameter(2, LocalDateTime.now().plusHours(1))
                .setParameter(3, 1L)
                .setParameter(4, 2L)
                .setParameter(5, Status.WAITING.toString())
                .executeUpdate();

        Booking booking2 = bookingRepository.findById(2L).orElseThrow(() -> {
            return new EntityNotFoundException(Booking.class, 2L);
        });
        Assertions.assertEquals(2L, booking2.getId());
        Assertions.assertEquals(1L, booking2.getItemId());

        List<Long> list = bookingRepository.getAllBookingItemsForUserStatus(1L, Status.WAITING);
        Assertions.assertEquals(2, list.size());

        List<Booking> listBookings = bookingRepository.findBookingsByBookerIdAndEndDateBeforeOrderByStartDateDesc(1L, LocalDateTime.now().plusHours(2));
        Assertions.assertEquals(1, listBookings.size());

        List<Long> bookingsCurrent = bookingRepository.findBookingsCurrent(1L, LocalDateTime.now().plusSeconds(20));
        Assertions.assertEquals(1, listBookings.size());
    }
}