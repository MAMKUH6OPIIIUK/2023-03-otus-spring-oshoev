package ru.otus.spring.homework.oke.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.exception.CautiousPersonException;
import ru.otus.spring.homework.oke.model.Egg;
import ru.otus.spring.homework.oke.model.Facehugger;
import ru.otus.spring.homework.oke.model.Human;

@Service
@Slf4j
public class HumanServiceImpl implements HumanService {
    private static final String[] NAMES = {"Вася", "Петя", "Коля"};

    @Override
    public Human examineEgg(Egg egg) {
        log.info("Человек прилетел на планету и нашел яйцо");
        String name = NAMES[RandomUtils.nextInt(0, NAMES.length)];
        if (isItSafe()) {
            log.info("{} решил заглянуть внутрь яйца", name);
            return new Human(name, new Facehugger());
        } else {
            log.info("{} осторожен, поэтому сжег яйцо", name);
            return new Human(name);
        }
    }

    @Override
    public void longLife(Human human) {
        log.info("И жил {} долго и счастливо", human.getName());
        throw new CautiousPersonException("Человек выжил");
    }

    private static boolean isItSafe() {
        return Math.random() < 0.5;
    }
}
