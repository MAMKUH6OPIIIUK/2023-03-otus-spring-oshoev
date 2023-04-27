package ru.otus.spring.homework.oke.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Student {
    private final String name;

    private final String surname;
}
