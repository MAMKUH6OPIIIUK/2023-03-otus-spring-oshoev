package ru.otus.spring.homework.oke.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.model.document.BookDocument;
import ru.otus.spring.homework.oke.model.entity.Book;
import ru.otus.spring.homework.oke.service.KeyValueCacheService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookItemProcessListener implements ItemProcessListener<Book, BookDocument> {
    private final KeyValueCacheService cacheService;

    private final BookMapper bookMapper;

    @Override
    public void afterProcess(@NonNull Book book, BookDocument processedBook) {
        String generatedBookIdKey = this.bookMapper.mapToCacheKey(book);
        assert processedBook != null;
        this.cacheService.putString(generatedBookIdKey, processedBook.getId());
        log.debug(String.format("Обработана книга. Данные по идентификаторам помещены в кэш. Исходный идентификатор: " +
                "%s, сгенерированный идентификатор: %s", book.getId(), processedBook.getId()));
    }
}
