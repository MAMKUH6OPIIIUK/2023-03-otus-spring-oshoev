package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.GenresDao;
import ru.otus.spring.homework.oke.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenresServiceImpl implements GenresService {
    private final GenresDao genresDao;

    @Override
    public Genre create(String name) {
        Genre genreToCreate = new Genre(0, name);
        return this.genresDao.create(genreToCreate);
    }

    @Override
    public Genre findById(long id) {
        return this.genresDao.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return this.genresDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        this.genresDao.deleteById(id);
    }
}
