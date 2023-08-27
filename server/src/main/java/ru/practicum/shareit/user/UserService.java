package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.*;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto findUserDtoById(Long id) throws EntityNotFoundException;

    void deleteUser(Long id) throws EntityNotFoundException;

    UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists;

    UserDto updateUser(UserDto userDto, Long id) throws EmailAlreadyExists, EntityNotFoundException;
}