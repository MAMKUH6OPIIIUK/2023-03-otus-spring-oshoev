package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorRequestDto {
    private String name;

    private String middleName;

    private String patronymic;

    private String surname;
}
