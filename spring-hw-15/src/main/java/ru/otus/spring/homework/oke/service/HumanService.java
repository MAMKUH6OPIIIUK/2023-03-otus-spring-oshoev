package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.model.Egg;
import ru.otus.spring.homework.oke.model.Human;

public interface HumanService {
    Human examineEgg(Egg egg);

    void longLife(Human human);
}
