package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.*;

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
    public ItemDto addNewItem(ItemDto itemDto, Long userId) throws ItemNotFound {
        Item newItem = ItemMapper.toDtoItem(itemDto);
        User user = userRepository.getUser(userId);
        if (user == null) {
            log.error("Error generate: ItemNotFound");
            throw new ItemNotFound();
        }
        id++;
        newItem.setId(id);
        newItem.setOwner(user);
        items.add(newItem);
        log.info("Добавлен Item: {}", newItem);
        return ItemMapper.toItemDto(newItem);
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
    public ItemDto getItemDto(Long id) throws ItemNotFound {
        Item item = getItem(id);
        if (item == null) {
            log.error("Error generate: ItemNotFound");
            throw new ItemNotFound();
        }
        log.info("Возвращен Item с id: {}", id);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteItem(Long id) {
        items.removeIf(item -> item.getId().equals(id));
        log.info("Удален Item с id: {}", id);
    }

    @Override
    public ItemDto updateItem(ItemDto item, Long id, Long userId) throws ItemNotFound, UserWithIdNotFound, UserNotHaveThisItem {
        Item itemForUpdate = getItem(id);
        if (itemForUpdate == null) {
            log.error("Error generate: ItemNotFound");
            throw new ItemNotFound();
        }
        User user = userRepository.getUser(userId);
        if (user == null) {
            log.error("Error generate: UserWithIdNotFound");
            throw new UserWithIdNotFound();
        }
        if (!userId.equals(itemForUpdate.getOwner().getId())) {
            log.error("Error generate: UserWithIdNotFound");
            throw new UserNotHaveThisItem(userId, id);
        }
        if (item.getName() != null) {
            itemForUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemForUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemForUpdate.setAvailable(item.getAvailable());
        }
        log.info("Item обновлен: {}", itemForUpdate);
        return ItemMapper.toItemDto(itemForUpdate);
    }
}