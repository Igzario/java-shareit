package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.getAllUsers();
    }

    @Override
    public UserDto getUserDto(Long id) throws UserWithIdNotFound {
        return repository.getUserDto(id);
    }

    @Override
    public void deleteUser(Long id) throws UserWithIdNotFound {
        repository.deleteUser(id);
    }

    @Override
    public UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists {
        return repository.addNewUser(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) throws EmailAlreadyExists, UserWithIdNotFound {
        return repository.updateUser(userDto, id);
    }
}