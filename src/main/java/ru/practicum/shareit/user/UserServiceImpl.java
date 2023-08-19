package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Repository
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public ArrayList<UserDto> getAllUsers() {
        List<User> usersList = repository.findAll();
        ArrayList<UserDto> usersDtoList = new ArrayList<>();
        for (User user : usersList) {
            usersDtoList.add(UserMapper.toUserDto(user));
        }
        Collections.reverse(usersList);
        log.info("Возвращен список пользователей: {}", usersList);
        return usersDtoList;
    }


    @Transactional(readOnly = true)
    @Override
    public UserDto findUserDtoById(Long id) throws EntityNotFoundException {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
        log.info("Возвращен пользователь: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        try {
            log.info("Удален пользователь с ID: {}", id);
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Сгенерирован EntityNotFoundException");
            throw new EntityNotFoundException(User.class, id);
        }
    }

    @Transactional
    @Override
    public UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists {
        User user = UserMapper.toDtoUser(userDto);
        try {
            log.info("Добавлен пользователь: {}", user);
            return UserMapper.toUserDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            log.info("Сгенерирован EmailAlreadyExists");
            throw new EmailAlreadyExists();
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long id) throws EmailAlreadyExists, EntityNotFoundException {
        User user = UserMapper.toDtoUser(userDto);
        User userForUpdate = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        try {
            if (user.getEmail() != null) {
                if (userForUpdate.getEmail() == user.getEmail()) {
                    log.info("Пользователь обновлен: {}", userForUpdate);
                    return UserMapper.toUserDto(userForUpdate);
                }
                userForUpdate.setEmail(user.getEmail());
            }
        } catch (DataIntegrityViolationException e) {
            log.info("Сгенерирован EmailAlreadyExists");
            throw new EmailAlreadyExists();
        }
        log.info("Пользователь обновлен: {}", userForUpdate);
        return UserMapper.toUserDto(userForUpdate);
    }
}