package ru.otus.spring.homework.oke.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class Author {
    @Id
    private String id;

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
