package ru.practicum.shareit.comment.dto;
import lombok.Data;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = ("(?i).*[a-zа-я].*"), groups = Update.class)
    @NotBlank(message = "Ошибка ввода - пустое поле text", groups = Create.class)
    private String text;
    private String authorName;
    private LocalDateTime created;
}