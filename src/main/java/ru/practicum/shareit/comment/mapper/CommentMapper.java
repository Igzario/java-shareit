package ru.practicum.shareit.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment, User autor) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                autor.getName(),
                comment.getCreated()
        );
    }

    public Comment toComment(CommentDto commentDto, Item item, User author) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItemId(item.getId());
        comment.setAuthorId(author.getId());
        comment.setCreated(LocalDateTime.now());
        return comment;

    }
}