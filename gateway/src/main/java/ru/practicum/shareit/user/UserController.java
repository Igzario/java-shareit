package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validated.*;
import ru.practicum.shareit.user.dto.UserDto;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Запрос на вывод спика пользователей");
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addNewUser(@Validated @RequestBody UserDto userDto) {
        log.info("Запрос на добавление пользователя {}", userDto);
        return userClient.addNewUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@Validated(Update.class) @RequestBody UserDto userDto,
                                             @PathVariable long id) {
        log.info("Запрос на обновление пользователя с ID {}", id);
        return userClient.updateUser(id, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        log.info("Запрос на вывод пользователя с ID {}", id);
        return userClient.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("Запрос на удаление пользователя с ID {}", id);
        userClient.deleteUser(id);
    }
}