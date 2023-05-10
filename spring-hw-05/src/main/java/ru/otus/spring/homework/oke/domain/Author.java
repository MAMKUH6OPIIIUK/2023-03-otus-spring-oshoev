package ru.otus.spring.homework.oke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Author {
    private long id;

    private String name;

    private String middleName;

    private String patronymic;

    private String surname;

    public Author(String name, String middleName, String patronymic, String surname) {
        this.name = name;
        this.middleName = middleName;
        this.patronymic = patronymic;
        this.surname = surname;
    }
}
