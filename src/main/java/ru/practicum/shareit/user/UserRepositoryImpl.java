package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Set<User> users = new HashSet<>();
    private Long id = 0L;

    @Override
    public List<UserDto> getAllUsers() {
        ArrayList<UserDto> usersList = new ArrayList<>();
        for (User user : users) {
            usersList.add(UserMapper.toUserDto(user));
        }
        Collections.reverse(usersList);
        log.info("Возвращен список всех пользователей");
        return usersList;
    }

    @Override
    public User getUser(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public ResponseEntity getUserDto(Long id) {
        User user = getUser(id);
        if (user == null) {
            String error = "Пользователь с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(409));
        }
        log.info("Возвращен пользователь с id: {}", id);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity deleteUser(Long id) {
        User user = getUser(id);
        if (user == null) {
            String error = "Пользователь с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(409));
        }
        users.remove(user);
        log.info("Удален пользователь с id: {}", id);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity addNewUser(User user) {
        if (validEmailAlreadyExists(user) != user) {
            String error = "Пользователь с таким email уже существует";
            log.error(error + ": {}", user);
            return new ResponseEntity<>(HttpStatus.valueOf(409));
        }
        id++;
        user.setId(id);
        users.add(user);
        log.info("Добавлен пользователь: {}", user);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity updateUser(User user, Long id) {
        User userForUpdate = getUser(id);
        if (userForUpdate == null) {
            String error = "Пользователь с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(409));
        }
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!isEmail(user.getEmail())) {
                String error = "Email указан не верно";
                log.error(error + ": {}", user.getEmail());
                return new ResponseEntity<>(error, HttpStatus.valueOf(500));
            } else if (validEmailAlreadyExists(user) != user && !Objects.equals(validEmailAlreadyExists(user).getId(), id)) {
                String error = "Пользователь с таким email уже существует";
                log.error(error + ": {}", user);
                return new ResponseEntity<>(error, HttpStatus.valueOf(409));
            } else {
                userForUpdate.setEmail(user.getEmail());
            }
        }
        log.info("Пользователь обновлен: {}", userForUpdate);
        return new ResponseEntity<>(UserMapper.toUserDto(userForUpdate), HttpStatus.valueOf(200));
    }

    private User validEmailAlreadyExists(User userCheck) {
        for (User user : users) {
            if (user.getEmail().equals(userCheck.getEmail())) {
                return user;
            }
        }
        return userCheck;
    }

    public static boolean isEmail(String s) {
        final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return EMAIL.matcher(s).matches();
    }
}