package ru.practicum.shareit.repositoryTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testRepository() throws EntityNotFoundException {
        EntityManager em1 = em.getEntityManager();

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(null);
        itemDto.setName("ItemName");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);

        em1.createNativeQuery("INSERT INTO USERS (NAME, EMAIL)" +
                        "VALUES (?,?)")
                .setParameter(1, "Alex")
                .setParameter(2, "alex@email.com")
                .executeUpdate();

        em1.createNativeQuery("INSERT INTO ITEMS (NAME, DESCRIPTION, AVAILABLE, OWNER_ID, REQUEST_ID)" +
                        "VALUES  (?,?,?,?,?)")
                .setParameter(1, itemDto.getName())
                .setParameter(2, itemDto.getDescription())
                .setParameter(3, itemDto.getAvailable())
                .setParameter(4, userDto.getId())
                .setParameter(5, null)
                .executeUpdate();

        Item item = new Item();
        item.setName("item1");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(1L);

        em.persist(item);
        Item itemSearch1 = itemRepository.findById(1L).orElseThrow(() -> {
            return new EntityNotFoundException(Item.class, 1L);
        });

        Assertions.assertEquals(1L, itemSearch1.getId());
        Assertions.assertEquals("ItemName", itemSearch1.getName());

        Item itemSearch2 = itemRepository.findById(2L).orElseThrow(() -> {
            return new EntityNotFoundException(Item.class, 2L);
        });

        Assertions.assertEquals(2L, itemSearch2.getId());
        Assertions.assertEquals("item1", itemSearch2.getName());
    }
}