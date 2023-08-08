package ru.otus.spring.homework.oke.config.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.spring.homework.oke.service.DropDBService;

@Configuration
@RequiredArgsConstructor
public class DropDBStepConfig {
    private static final String STEP_NAME = "dropDBStep";

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DropDBService dropDBService;

    @Bean
    public MethodInvokingTaskletAdapter dropDBTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(this.dropDBService);
        adapter.setTargetMethod("dropDb");
        return adapter;
    }

    @SuppressWarnings("unused")
    @Bean
    public Step dropDBStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(dropDBTasklet(), platformTransactionManager)
                .build();
    }
}
