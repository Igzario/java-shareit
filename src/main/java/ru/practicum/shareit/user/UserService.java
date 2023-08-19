package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.*;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserDto(Long id) throws UserWithIdNotFound;

    void deleteUser(Long id) throws UserWithIdNotFound;

    UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists;

    UserDto updateUser(UserDto userDto, Long id) throws EmailAlreadyExists, UserWithIdNotFound;
}