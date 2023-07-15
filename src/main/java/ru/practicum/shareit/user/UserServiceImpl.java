package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

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
    public ResponseEntity getUserDto(Long id) {
        return repository.getUserDto(id);
    }

    @Override
    public ResponseEntity deleteUser(Long id) {
        return repository.deleteUser(id);
    }

    @Override
    public ResponseEntity addNewUser(User user) {
        return repository.addNewUser(user);
    }

    @Override
    public ResponseEntity updateUser(User user, Long id) {
        return repository.updateUser(user, id);
    }
}