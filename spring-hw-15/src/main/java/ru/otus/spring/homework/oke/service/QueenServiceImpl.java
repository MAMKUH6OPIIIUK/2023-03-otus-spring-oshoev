package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.integration.AlienGateway;
import ru.otus.spring.homework.oke.model.Egg;
import ru.otus.spring.homework.oke.model.Xenomorph;

import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueenServiceImpl implements QueenService {
    private final AlienGateway alienGateway;

    @Override
    public void startReproductionCycle() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 15; i++) {
            int num = i + 1;
            pool.execute(() -> {
                Egg egg = new Egg();
                log.info("Отложено яйцо №{}: {}", num, egg);
                try {
                    Xenomorph xenomorph = alienGateway.processLifeCycle(egg);
                    log.info("Яйцо №{} превратилось в ксеноморфа: {}", num, xenomorph);
                } catch (Exception e) {
                    log.error("Яйцо №{} не превратилось в ксеноморфа, потому что: {}", num, e.getMessage());
                }
            });
            delay();
        }
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
