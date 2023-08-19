package ru.practicum.shareit.comment.mapper;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment, User autor) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                autor.getName(),
                comment.getCreated()
        );
    }
    public static Comment toComment(CommentDto commentDto, Item item, User autor) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItemId(item.getId());
        comment.setAuthorId(autor.getId());
        comment.setCreated(LocalDateTime.now());
        return comment;

    }
}