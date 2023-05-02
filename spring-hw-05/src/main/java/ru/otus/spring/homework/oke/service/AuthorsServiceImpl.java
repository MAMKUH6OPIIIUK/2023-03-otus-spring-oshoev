package ru.otus.spring.homework.oke.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.AuthorsDao;
import ru.otus.spring.homework.oke.domain.Author;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorsDao authorsDao;

    @Override
    public Author create(String name, String middleName, String patronymic, String surname) {
        Author authorToCreate = new Author(0, name, middleName, patronymic, surname);
        return this.authorsDao.create(authorToCreate);
    }

    @Override
    public Author findById(long id) {
        return this.authorsDao.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return this.authorsDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        this.authorsDao.deleteById(id);
    }
}
