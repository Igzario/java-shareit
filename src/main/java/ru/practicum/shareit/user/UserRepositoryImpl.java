package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.*;

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
    public UserDto getUserDto(Long id) throws UserWithIdNotFound {
        User user = getUser(id);
        if (user == null) {
            log.error("Error generate: UserWithIdNotFound");
            throw new UserWithIdNotFound();
        }
        log.info("Возвращен пользователь с id: {}", id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) throws UserWithIdNotFound {
        User user = getUser(id);
        if (user == null) {
            log.error("Error generate: UserWithIdNotFound");
            throw new UserWithIdNotFound();
        }
        users.remove(user);
        log.info("Удален пользователь с id: {}", id);
    }

    @Override
    public UserDto addNewUser(UserDto userDto) throws EmailAlreadyExists {
        User user = UserMapper.toDtoUser(userDto);
        if (validEmailAlreadyExists(user) != user) {
            log.error("Error generate: EmailAlreadyExists");
            throw new EmailAlreadyExists();
        }
        id++;
        user.setId(id);
        users.add(user);
        log.info("Добавлен пользователь: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) throws EmailAlreadyExists, UserWithIdNotFound {
        User user = UserMapper.toDtoUser(userDto);
        User userForUpdate = getUser(id);
        if (userForUpdate == null) {
            log.error("Error generate: UserWithIdNotFound");
            throw new UserWithIdNotFound();
        }
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (validEmailAlreadyExists(user) != user && !Objects.equals(validEmailAlreadyExists(user).getId(), id)) {
                log.error("Error generate: UserWithEmailAlreadyExists");
                throw new EmailAlreadyExists();
            } else {
                userForUpdate.setEmail(user.getEmail());
            }
        }
        log.info("Пользователь обновлен: {}", userForUpdate);
        return UserMapper.toUserDto(userForUpdate);
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