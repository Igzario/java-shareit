package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(Long id);

    List<Item> findItemsByDescriptionContainingIgnoreCaseAndAvailableTrue(String text);

    List<Item> findItemsByRequest(Long requestId);
}