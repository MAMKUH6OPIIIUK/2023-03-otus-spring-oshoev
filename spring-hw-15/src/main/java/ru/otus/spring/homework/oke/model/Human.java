package ru.otus.spring.homework.oke.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class Human {
    private final String name;

    private final Facehugger facehugger;

    public Human(String name) {
        this.name = name;
        this.facehugger = null;
    }
}