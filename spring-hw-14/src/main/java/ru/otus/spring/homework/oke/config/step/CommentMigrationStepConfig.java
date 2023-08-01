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
import ru.otus.spring.homework.oke.listener.CommentItemProcessListener;
import ru.otus.spring.homework.oke.model.document.BookDocument;
import ru.otus.spring.homework.oke.model.document.CommentDocument;
import ru.otus.spring.homework.oke.model.entity.Comment;
import ru.otus.spring.homework.oke.processor.CommentItemProcessor;

@SuppressWarnings("unused")
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommentMigrationStepConfig {
    public static final String STEP_NAME = "commentsMigrationStep";

    private static final int CHUNK_SIZE = 5;

    private final JobRepository jobRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoOperations mongoOperations;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaPagingItemReader<Comment> commentReader() {
        return new JpaPagingItemReaderBuilder<Comment>()
                .name("bookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Comment c order by c.id")
                .build();
    }

    @Bean
    public MongoItemWriter<CommentDocument> commentWriter() {
        return new MongoItemWriterBuilder<CommentDocument>()
                .collection("comments")
                .template(mongoOperations)
                .build();
    }

    @Bean
    public Step commentsMigrationStep(ItemReader<Comment> commentReader, ItemWriter<CommentDocument> commentWriter,
                                      CommentItemProcessor commentProcessor,
                                      CommentItemProcessListener processListener) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Comment, CommentDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.error("Ошибка при чтении комментария", ex);
                    }
                })
                .listener(processListener)
                .listener(new ItemWriteListener<BookDocument>() {
                    @Override
                    public void onWriteError(@NonNull Exception exception,
                                             @NonNull Chunk<? extends BookDocument> items) {
                        log.error("Ошибка при записи пачки комментариев", exception);
                    }
                })
                .build();
    }
}
