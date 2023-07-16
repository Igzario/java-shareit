package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    final ItemRepositoryImpl itemRepository;

    @Override
    public ItemDto getItemDto(Long id) throws ItemNotFound {
        return itemRepository.getItemDto(id);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteItem(id);
    }

    @Override
    public ItemDto addNewItem(ItemDto item, Long userId) throws ItemNotFound {
        return itemRepository.addNewItem(item, userId);
    }

    @Override
    public ItemDto updateItem(ItemDto item, Long id, Long userId) throws UserWithIdNotFound, ItemNotFound, UserNotHaveThisItem {
        return itemRepository.updateItem(item, id, userId);
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        ArrayList<ItemDto> itemsList = new ArrayList<>();
        for (Item item : itemRepository.getItems()) {
            if (Objects.equals(item.getOwner().getId(), userId)) {
                itemsList.add(ItemMapper.toItemDto(item));
            }
        }
        log.info("Возвращен список Item пользователя: {}", userId);
        return itemsList;
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String search) {
        ArrayList<ItemDto> itemsList = new ArrayList<>();
        String name;
        String description;
        if (search.isEmpty()) {
            log.info("Возвращен пустой список Item после поиска по пустому слову");
            return itemsList;
        }
        for (Item item : itemRepository.getItems()) {
            search = search.toLowerCase();
            name = item.getName().toLowerCase();
            description = item.getDescription().toLowerCase();
            if ((name.contains(search) || description.contains(search)) && item.getAvailable()) {
                itemsList.add(ItemMapper.toItemDto(item));
            }
        }
        Collections.reverse(itemsList);
        log.info("Возвращен список Item после поиска по слову: {}", search);
        return itemsList;
    }
}