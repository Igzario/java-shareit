package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItemOwner;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

@Data
public class ItemDtoWithComments {
    private Long id;

    @Pattern(regexp = ("(?i).*[a-zа-я].*"), groups = Update.class)
    @NotBlank(message = "Ошибка ввода - пустое поле Name", groups = Create.class)
    private String name;

    @Pattern(regexp = ("(?i).*[a-zа-я].*"), groups = Update.class)
    @NotBlank(message = "Ошибка ввода - пустое поле Description", groups = Create.class)
    private String description;

    @NotNull(message = "Ошибка ввода - Available: null", groups = Create.class)
    private Boolean available;
    private BookingDtoForItemOwner lastBooking;
    private BookingDtoForItemOwner nextBooking;
    private ArrayList<CommentDto> comments;
}