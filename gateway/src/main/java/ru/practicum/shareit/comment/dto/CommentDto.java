package ru.practicum.shareit.comment.dto;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    @NotBlank(message = "Ошибка ввода - пустое поле text")
    private String text;
    private String authorName;
    private LocalDateTime created;
}