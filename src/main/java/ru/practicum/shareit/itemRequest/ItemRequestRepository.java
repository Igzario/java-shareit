package ru.practicum.shareit.itemRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.itemRequest.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findItemRequestsByRequestorId(Long userId);

    Page<ItemRequest> findByRequestorIdIsNot(Long ownerId, Pageable pageable);
}
