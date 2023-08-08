package ru.otus.spring.homework.oke.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.homework.oke.config.JobConfig;

import java.util.Properties;

@ShellComponent
@RequiredArgsConstructor
public class JobCommands {
    private final JobOperator jobOperator;

    private Long executionId;

    @ShellMethod(value = "Запустить миграцию данных библиотеки", key = {"smj", "start migration job"})
    public String startMigrationJob() throws Exception {
        Properties jobParameters = new Properties();
        this.executionId = jobOperator.start(JobConfig.JOB_NAME, jobParameters);
        return String.format("Задача %s выполнена", jobOperator.getSummary(executionId));

    }

    @ShellMethod(value = "Перезапустить миграцию данных библиотеки", key = {"rmj", "restart migration job"})
    @ShellMethodAvailability(value = "existsExecution")
    public String restartMigrationJob() throws Exception {
        jobOperator.restart(this.executionId);
        return String.format("Задача %s перезапущена", jobOperator.getSummary(executionId));
    }

    private Availability existsExecution() {
        String errorMessage = "Сначала запустите джоб на выполнение";
        return this.executionId == null ? Availability.unavailable(errorMessage) : Availability.available();
    }

}
