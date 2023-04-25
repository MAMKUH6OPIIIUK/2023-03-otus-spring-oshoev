package ru.otus.spring.homework.oke.dao;

import ru.otus.spring.homework.oke.domain.Testing;

import java.util.List;

public interface TestingDao {
    List<Testing> findAll();
}
