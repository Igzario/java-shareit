package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

interface UserRepository {
    List<UserDto> getAllUsers();

    User getUser(Long id);

    ResponseEntity getUserDto(Long id);

    ResponseEntity deleteUser(Long id);

    ResponseEntity addNewUser(User user);

    ResponseEntity updateUser(User user, Long id);
}