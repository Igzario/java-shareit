package ru.practicum.shareit.itemRequest.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "requestor_id")
    private Long requestorId;
    @NotBlank(message = "Ошибка ввода - пустое поле Description")
    private String description;
    @NonNull
    private LocalDateTime created;

    public ItemRequest() {
    }
}