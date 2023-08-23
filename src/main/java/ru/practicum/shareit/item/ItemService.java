package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.List;

public interface ItemService {

    List<ItemDtoForOwner> getItemsByUser(Long userId);

    Object getItemDto(Long itemId, Long userID) throws EntityNotFoundException;

    void deleteItem(Long id);

    ItemDto addNewItem(ItemDto item, Long userId) throws EntityNotFoundException;

    ItemDto updateItem(ItemDto item, Long id, Long userId) throws UserNotHaveThisItemException, EntityNotFoundException;

    List<ItemDto> searchItem(Long userId, String search);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) throws EntityNotFoundException, AddCommentException;
}