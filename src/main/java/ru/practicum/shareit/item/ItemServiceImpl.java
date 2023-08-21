package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoForItemOwner;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public Object getItemDto(Long itemId, Long userID) throws EntityNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(Item.class, userID));
        BookingDtoForItemOwner lastBooking = null;
        BookingDtoForItemOwner nextBooking = null;
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        ArrayList<CommentDto> commentDtoList = new ArrayList<>();
        comments.forEach(comment -> {
            User autor = userRepository.findById(comment.getAuthorId()).orElse(new User());
            commentDtoList.add(CommentMapper.toCommentDto(
                    comment, autor));
        });
        if (item.getOwner().equals(userID)) {
            List<Booking> bookings = bookingRepository.findBookingByItemIdAndStatusIsNotAndEndDateAfterOrderByStartDate(itemId, Status.REJECTED, LocalDateTime.now().minusSeconds(5));
            if (bookings.isEmpty()) {
                return ItemMapper.toItemDtoWithComments(item, lastBooking, nextBooking, commentDtoList);
            }
            lastBooking = BookingMapper.bookingDtoForItemOwner(bookings.get(0));
            if (bookings.size() > 2) {
                nextBooking = BookingMapper.bookingDtoForItemOwner(bookings.get(bookings.size() - 2));
            }
            return ItemMapper.toItemDtoWithComments(item, lastBooking, nextBooking, commentDtoList);
        }
        log.info("Возвращен Item: {}", item);
        return ItemMapper.toItemDtoWithComments(item, lastBooking, nextBooking, commentDtoList);
    }

    @Transactional
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
        log.info("Удален Item c id: {}", id);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(ItemDto itemDto, Long userId) throws EntityNotFoundException {
        Item item = ItemMapper.toDtoItem(itemDto);
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        item.setOwner(userId);
        log.info("Добавлен Item: {}", item);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto item, Long id, Long userId) throws EntityNotFoundException, UserNotHaveThisItemException {
        Item itemForUpdate = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Item.class, id));
        if (itemForUpdate.getOwner().equals(userId)) {
            if (item.getName()!=null && !item.getName().isBlank()) {
                itemForUpdate.setName(item.getName());
            }
            if (item.getDescription()!=null && !item.getDescription().isBlank()) {
                itemForUpdate.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                itemForUpdate.setAvailable(item.getAvailable());
            }

        } else {
            log.info("Сгенерирован UserNotHaveThisItemException");
            throw new UserNotHaveThisItemException(userId, id);
        }
        log.info("Item обновлен: {}", itemForUpdate);
        return ItemMapper.toItemDto(itemForUpdate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoForOwner> getItemsByUser(Long userId) {
        List<Item> items = itemRepository.findByOwner(userId);
        List<ItemDtoForOwner> itemsList = new ArrayList<>();
        items.forEach(item -> {
            if (item.getOwner().equals(userId)) {
                List<Booking> bookings = bookingRepository.findBookingByItemId(item.getId());
                if (bookings.isEmpty()) {
                    itemsList.add(ItemMapper.toItemDtoForOwner(item, null, null));
                } else {
                    BookingDtoForItemOwner lastBooking = BookingMapper.bookingDtoForItemOwner(bookings.get(0));
                    BookingDtoForItemOwner nextBooking = BookingMapper.bookingDtoForItemOwner(bookings.get(bookings.size() - 1));
                    itemsList.add(ItemMapper.toItemDtoForOwner(item, lastBooking, nextBooking));
                }
            } else {
                itemsList.add(ItemMapper.toItemDtoForOwner(item, null, null));
            }
        });
        log.info("Возвращен список Item пользователя: {}", userId);
        return itemsList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItem(Long userId, String search) {
        if (search.isEmpty()) {
            log.info("Возвращен пустой список Item после поиска по пустому слову");
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.findItemsByDescriptionContainingIgnoreCaseAndAvailableTrue(search);
        List<ItemDto> itemsList = new ArrayList<>();
        items.forEach(item -> itemsList.add(ItemMapper.toItemDto(item)));
        log.info("Возвращен список Item после поиска по слову: {}", search);
        return itemsList;
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) throws EntityNotFoundException, AddCommentException {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(Item.class, itemId));
        Comment comment = null;
        List<Long> bookingList = bookingRepository.findBookingsToAddComment(userId, itemId, LocalDateTime.now(), Status.APPROVED);
        if (!bookingList.isEmpty()) {
            comment = CommentMapper.toComment(commentDto, item, user);
            commentRepository.save(comment);
            return CommentMapper.toCommentDto(comment, user);
        } else {
            log.info("Сгенерирован AddCommentException");
            throw new AddCommentException();
        }
    }
}