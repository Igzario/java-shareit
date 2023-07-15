package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    final ItemRepositoryImpl itemRepository;

    @Override
    public ResponseEntity getItem(Long id) {
        return itemRepository.getItemDto(id);
    }


    @Override
    public ResponseEntity deleteItem(Long id) {
        return itemRepository.deleteItem(id);
    }

    @Override
    public ResponseEntity addNewItem(ItemDto item, Long userId) {
        return itemRepository.addNewItem(item, userId);
    }

    @Override
    public ResponseEntity updateItem(ItemDto item, Long id, Long userId) {
        return itemRepository.updateItem(item, id, userId);
    }

    @Override
    public ResponseEntity getItemsByUser(Long userId) {
        ArrayList<ItemDto> itemsList = new ArrayList<>();
        for (Item item : itemRepository.getItems()) {
            if (Objects.equals(item.getOwner().getId(), userId)) {
                itemsList.add(ItemMapper.toItemDto(item));
            }
        }
        log.info("Возвращен список Item пользователя: {}", userId);
        return new ResponseEntity<>(itemsList, HttpStatus.valueOf(200));
    }

    @Override
    public ResponseEntity searchItem(Long userId, String search) {
        ArrayList<ItemDto> itemsList = new ArrayList<>();
        String name;
        String description;
        if (search.isEmpty()) {
            log.info("Возвращен пустой список Item после поиска по пустому слову");
            return new ResponseEntity<>(itemsList, HttpStatus.valueOf(200));
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
        return new ResponseEntity<>(itemsList, HttpStatus.valueOf(200));
    }
}