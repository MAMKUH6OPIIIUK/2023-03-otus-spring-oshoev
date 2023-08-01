package ru.otus.spring.homework.oke.config.step;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.spring.homework.oke.listener.AuthorItemProcessListener;
import ru.otus.spring.homework.oke.model.document.AuthorDocument;
import ru.otus.spring.homework.oke.model.entity.Author;
import ru.otus.spring.homework.oke.processor.AuthorItemProcessor;

@SuppressWarnings("unused")
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorMigrationStepConfig {
    public static final String STEP_NAME = "authorsMigrationStep";

    private static final int CHUNK_SIZE = 5;

    private final JobRepository jobRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoOperations mongoOperations;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("genreItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from Author a order by a.id")
                .build();
    }

    @Bean
    public MongoItemWriter<AuthorDocument> authorWriter() {
        return new MongoItemWriterBuilder<AuthorDocument>()
                .collection("authors")
                .template(mongoOperations)
                .build();
    }

    @Bean
    public Step authorsMigrationStep(ItemReader<Author> authorReader, ItemWriter<AuthorDocument> authorWriter,
                                     AuthorItemProcessor authorProcessor, AuthorItemProcessListener processListener) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Author, AuthorDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.error("Ошибка при чтении автора", ex);
                    }
                })
                .listener(processListener)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(@NonNull Exception exception,
                                             @NonNull Chunk<? extends AuthorDocument> items) {
                        log.error("Ошибка при записи пачки авторов", exception);
                    }
                })
                .build();
    }
}
