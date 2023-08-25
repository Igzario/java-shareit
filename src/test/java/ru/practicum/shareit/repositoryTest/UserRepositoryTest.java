package ru.practicum.shareit.repositoryTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testRepository() throws EntityNotFoundException {
        EntityManager em1 = em.getEntityManager();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        em1.createNativeQuery("INSERT INTO USERS (NAME, EMAIL)" +
                        "VALUES (?,?)")
                .setParameter(1, "Alex")
                .setParameter(2, "alex@email.com")
                .executeUpdate();


        User user = new User();
        user.setName("Oleg");
        user.setEmail("ol@mail.com");

        User userCheck = userRepository.findById(1L).orElseThrow(() -> {
            return new EntityNotFoundException(User.class, 1L);
        });

        Assertions.assertEquals(1L, userCheck.getId());
        Assertions.assertEquals("Alex", userCheck.getName());

        em.persist(user);
        userCheck = userRepository.findById(2L).orElseThrow(() -> {
            return new EntityNotFoundException(User.class, 1L);
        });

        Assertions.assertEquals(2L, userCheck.getId());
        Assertions.assertEquals("Oleg", userCheck.getName());
    }
}