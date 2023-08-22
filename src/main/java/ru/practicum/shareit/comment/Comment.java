package ru.practicum.shareit.comment;

import lombok.Data;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Ошибка ввода - пустое поле text", groups = Create.class)
    private String text;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "author_id")
    private Long authorId;
    private LocalDateTime created;
}