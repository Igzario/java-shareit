package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoForItemOwner;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest());
        } else {
            itemDto.setRequestId(null);
        }
        return itemDto;
    }

    public Item toDtoItem(ItemDto itemDto) {
        Item newItem = new Item();
        newItem.setId(itemDto.getId());
        newItem.setName(itemDto.getName());
        newItem.setDescription(itemDto.getDescription());
        newItem.setAvailable(itemDto.getAvailable());
        newItem.setRequest(itemDto.getRequestId());
        return newItem;
    }

    public ItemDtoForOwner toItemDtoForOwner(Item item, BookingDtoForItemOwner lastBooking, BookingDtoForItemOwner nextBooking) {
        ItemDtoForOwner itemDtoForOwner = new ItemDtoForOwner(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
        itemDtoForOwner.setNextBooking(nextBooking);
        itemDtoForOwner.setLastBooking(lastBooking);
        return itemDtoForOwner;
    }

    public ItemDtoWithComments toItemDtoWithComments(Item item, BookingDtoForItemOwner lastBooking, BookingDtoForItemOwner nextBooking, ArrayList<CommentDto> comments) {
        ItemDtoWithComments itemDtoWithComments = new ItemDtoWithComments();
        itemDtoWithComments.setId(item.getId());
        itemDtoWithComments.setName(item.getName());
        itemDtoWithComments.setDescription(item.getDescription());
        itemDtoWithComments.setAvailable(item.getAvailable());
        itemDtoWithComments.setNextBooking(nextBooking);
        itemDtoWithComments.setLastBooking(lastBooking);
        itemDtoWithComments.setComments(comments);
        return itemDtoWithComments;
    }
}