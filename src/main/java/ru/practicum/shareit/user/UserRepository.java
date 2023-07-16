package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.*;

import java.util.List;

interface UserRepository {
    List<UserDto> getAllUsers();

    User getUser(Long id);

    UserDto getUserDto(Long id) throws UserWithIdNotFound;

    void deleteUser(Long id) throws UserWithIdNotFound;

    UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists;

    UserDto updateUser(UserDto userDto, Long id) throws EmailAlreadyExists, UserWithIdNotFound;
}