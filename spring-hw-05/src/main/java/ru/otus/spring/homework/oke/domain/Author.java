package ru.otus.spring.homework.oke.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Author {
    private final long id;

    private final String name;

    private final String middleName;

    private final String patronymic;

    private final String surname;
}
