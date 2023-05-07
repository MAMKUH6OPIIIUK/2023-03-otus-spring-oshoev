package ru.otus.spring.homework.oke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Genre {
    private long id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }


}
