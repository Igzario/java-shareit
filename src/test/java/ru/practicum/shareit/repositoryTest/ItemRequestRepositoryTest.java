package ru.practicum.shareit.repositoryTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemRequest.ItemRequestRepository;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Mock
    private ItemRequestRepository itemRequestRepository;
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

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("desc");
        itemRequest.setRequestorId(1L);
        itemRequest.setCreated(LocalDateTime.now());
        em.persist(itemRequest);


        List<ItemRequest> itemRequestList = itemRequestRepository.findItemRequestsByRequestorId(1L);
        Assertions.assertEquals(1, itemRequestList.size());

        em1.createNativeQuery("INSERT INTO REQUESTS (REQUESTOR_ID, DESCRIPTION, CREATED)" +
                        "VALUES (?,?,?)")
                .setParameter(1, 1L)
                .setParameter(2, "desc2")
                .setParameter(3, LocalDateTime.now())
                .executeUpdate();

        itemRequestList = itemRequestRepository.findItemRequestsByRequestorId(1L);
        Assertions.assertEquals(2, itemRequestList.size());

        List<ItemRequest> requests =
                itemRequestRepository.findByRequestorIdIsNot(
                        2L, PageRequest.of(0, 10, Sort.by("created"))).getContent();
        Assertions.assertEquals(2, requests.size());
    }
}