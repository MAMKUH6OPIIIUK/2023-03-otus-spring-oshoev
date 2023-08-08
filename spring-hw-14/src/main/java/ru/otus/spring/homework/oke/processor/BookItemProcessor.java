package ru.otus.spring.homework.oke.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.model.document.BookDocument;
import ru.otus.spring.homework.oke.model.entity.Book;

@Component
@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<Book, BookDocument> {
    private final BookMapper bookMapper;

    @Override
    public BookDocument process(@NonNull Book book) {
        return this.bookMapper.mapToBookDocumentWithGeneratedId(book);
    }
}
