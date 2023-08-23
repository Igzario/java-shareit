package ru.practicum.shareit.item.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = ("(?i).*[a-zа-я\\d{10}].*"))
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;
    @Pattern(regexp = ("(?i).*[a-zа-я].*"))
    @NotBlank(message = "Ошибка ввода - пустое поле Description")
    private String description;
    @NotNull(message = "Ошибка ввода - Available: null")
    private Boolean available;
    @Column(name = "owner_id")
    private Long owner;
    @Column(name = "request_id")
    private Long request;

    public boolean isAvailable() {
        return this.available;
    }
}