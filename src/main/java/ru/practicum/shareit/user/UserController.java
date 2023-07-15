package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Запрос на вывод спика пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity addNewUser(@Valid @RequestBody User user) {
        log.info("Запрос на добавление пользователя {}", user);
        return userService.addNewUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable long id) {
        log.info("Запрос на обновление пользователя с ID {}", id);
        return userService.updateUser(user, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable long id) {
        log.info("Запрос на вывод пользователя с ID {}", id);
        return userService.getUserDto(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable long id) {
        log.info("Запрос на удаление пользователя с ID {}", id);
        return userService.deleteUser(id);
    }
}