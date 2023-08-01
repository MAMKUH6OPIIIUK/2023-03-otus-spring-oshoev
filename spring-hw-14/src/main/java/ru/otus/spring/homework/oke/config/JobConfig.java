package ru.otus.spring.homework.oke.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfig {
    public static final String JOB_NAME = "migrateLibraryJob";

    private final JobRepository jobRepository;

    @SuppressWarnings("unused")
    @Bean
    public Job migrateLibraryDatabase(Step authorsMigrationStep, Step booksMigrationStep, Step commentsMigrationStep,
                                      Step dropDBStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(dropDBStep)
                .next(authorsMigrationStep)
                .next(booksMigrationStep)
                .next(commentsMigrationStep)
                .end()
                .build();
    }
}
