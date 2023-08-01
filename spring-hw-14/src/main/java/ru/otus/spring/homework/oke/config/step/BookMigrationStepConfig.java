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
import ru.otus.spring.homework.oke.listener.BookItemProcessListener;
import ru.otus.spring.homework.oke.model.document.BookDocument;
import ru.otus.spring.homework.oke.model.entity.Book;
import ru.otus.spring.homework.oke.processor.BookItemProcessor;

@SuppressWarnings("unused")
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BookMigrationStepConfig {
    public static final String STEP_NAME = "booksMigrationStep";

    private static final int CHUNK_SIZE = 5;

    private final JobRepository jobRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoOperations mongoOperations;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaPagingItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Book b order by b.id")
                .build();
    }

    @Bean
    public MongoItemWriter<BookDocument> bookWriter() {
        return new MongoItemWriterBuilder<BookDocument>()
                .collection("books")
                .template(mongoOperations)
                .build();
    }

    @Bean
    public Step booksMigrationStep(ItemReader<Book> bookReader, ItemWriter<BookDocument> bookWriter,
                                   BookItemProcessor bookProcessor, BookItemProcessListener processListener) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Book, BookDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.error("Ошибка при чтении книги", ex);
                    }
                })
                .listener(processListener)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(@NonNull Exception exception,
                                             @NonNull Chunk<? extends BookDocument> items) {
                        log.error("Ошибка при записи пачки книг", exception);
                    }
                })
                .build();
    }
}
