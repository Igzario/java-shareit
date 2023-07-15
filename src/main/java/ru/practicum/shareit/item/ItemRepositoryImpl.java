package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryImpl;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final UserRepositoryImpl userRepository;
    @Getter
    private final Set<Item> items = new HashSet<>();
    private Long id = 0L;

    @Override
    public ResponseEntity addNewItem(ItemDto itemDto, Long userId) {
        Item newItem = ItemMapper.toDtoItem(itemDto);
        User user = userRepository.getUser(userId);
        if (user == null) {
            String error = "Пользователь с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
        id++;
        newItem.setId(id);
        newItem.setOwner(user);
        items.add(newItem);
        log.info("Добавлен Item: {}", newItem);
        return new ResponseEntity<>(ItemMapper.toItemDto(newItem), HttpStatus.valueOf(200));
    }

    @Override
    public Item getItem(Long id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public ResponseEntity getItemDto(Long id) {
        Item item = getItem(id);
        if (item == null) {
            String error = "Item с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
        log.info("Возвращен Item с id: {}", id);
        return new ResponseEntity<>(ItemMapper.toItemDto(item), HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity deleteItem(Long id) {
        items.removeIf(item -> item.getId().equals(id));
        log.info("Удален Item с id: {}", id);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity updateItem(ItemDto item, Long id, Long userId) {
        Item itemForUpdate = getItem(id);
        if (itemForUpdate == null) {
            String error = "Item с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
        User user = userRepository.getUser(userId);
        if (user == null) {
            String error = "Пользователь с таким id не существует";
            log.error(error + ": {}", id);
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
        if (userId != itemForUpdate.getOwner().getId()) {
            String error = "Пользователь с id {} не является владельцем вещи с id {}";
            log.error(error, userId, id);
            return new ResponseEntity<>(HttpStatus.valueOf(404));
        }
        if (item.getName() != null && !item.getName().isEmpty()) {
            itemForUpdate.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            itemForUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemForUpdate.setAvailable(item.getAvailable());
        }
        log.info("Item обновлен: {}", itemForUpdate);
        return new ResponseEntity<>(ItemMapper.toItemDto(itemForUpdate), HttpStatus.valueOf(200));
    }
}