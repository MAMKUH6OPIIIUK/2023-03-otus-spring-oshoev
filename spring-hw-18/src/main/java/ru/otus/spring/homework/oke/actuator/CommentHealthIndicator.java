package ru.otus.spring.homework.oke.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.service.CommentService;

import java.util.Date;

/**
 * Бизнесовая проверка здоровья приложения. Реализует контроль над количеством свежих комментариев к книгам - если
 * никто не комментирует, значит что-то пошло не так
 */
@Component
@RequiredArgsConstructor
public class CommentHealthIndicator implements HealthIndicator {
    private static final int HOURS_COUNT = 12;

    private final CommentService commentService;

    @Override
    public Health health() {
        Date thresholdDate = new Date(System.currentTimeMillis() - (HOURS_COUNT * 60 * 60 * 1000));
        long newCommentsCount = this.commentService.countByCreatedOnAfter(thresholdDate);
        if (newCommentsCount == 0) {
            String message = String.format("Нет новых комментариев к книгам за последние %d часов", HOURS_COUNT);
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", message)
                    .build();
        } else {
            String message = String.format("За последние %d часов создано комментариев к книгам: %d", HOURS_COUNT,
                    newCommentsCount);
            return Health.up()
                    .withDetail("message", message)
                    .build();
        }
    }
}
