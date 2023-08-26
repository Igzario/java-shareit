package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.mapper.ItemRequestMapper;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, Long userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });

        ItemRequest itemRequest = ItemRequestMapper.itemRequest(itemRequestDto, userId, LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getRequests(Long userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        List<ItemRequest> requests = itemRequestRepository.findItemRequestsByRequestorId(userId);
        return createRequestsDtoList(requests);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto getItemRequest(Long requestId, Long userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - Request");
            return new EntityNotFoundException(ItemRequest.class, requestId);
        });
        List<Item> items = itemRepository.findItemsByRequest(itemRequest.getId());
        List<ItemDto> itemsDto = new ArrayList<>();
        items.forEach(item -> itemsDto.add(ItemMapper.toItemDto(item)));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemsDto);
        return itemRequestDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getItemRequests(Long userId, Integer from, Integer size) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Сгенерирован EntityNotFoundException - User");
            return new EntityNotFoundException(User.class, userId);
        });
        if (size == null || from == null) {
            return new ArrayList<>();
        }
        List<ItemRequest> requests =
                itemRequestRepository.findByRequestorIdIsNot(
                        userId, PageRequest.of(from, size, Sort.by("created"))).getContent();
        return createRequestsDtoList(requests);
    }

    private List<ItemRequestDto> createRequestsDtoList(List<ItemRequest> requests) {
        List<ItemRequestDto> requestsDto = new ArrayList<>();
        if (requests.isEmpty()) {
            return requestsDto;
        }
        requests.forEach(itemRequest -> {
            List<Item> items = itemRepository.findItemsByRequest(itemRequest.getId());
            List<ItemDto> itemsDto = new ArrayList<>();
            items.forEach(item -> itemsDto.add(ItemMapper.toItemDto(item)));
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            itemRequestDto.setItems(itemsDto);
            requestsDto.add(itemRequestDto);
        });
        return requestsDto;
    }
}
