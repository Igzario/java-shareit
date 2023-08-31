package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = ("(?i).*[a-zа-я].*"))
    @NotBlank(message = "Ошибка ввода - пустое поле Name")
    private String name;

    @Email(message = "Ошибка ввода - Email: not email format")
    @NotBlank(message = "Ошибка ввода - пустое поле email")
    private String email;
}