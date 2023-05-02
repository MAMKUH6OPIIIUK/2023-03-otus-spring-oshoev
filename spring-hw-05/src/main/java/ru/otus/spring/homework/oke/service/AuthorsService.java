package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.domain.Author;

import java.util.List;

public interface AuthorsService {
    Author create(String name, String middleName, String patronymic, String surname);

    Author findById(long id);

    List<Author> findAll();

    void deleteById(long id);
}
