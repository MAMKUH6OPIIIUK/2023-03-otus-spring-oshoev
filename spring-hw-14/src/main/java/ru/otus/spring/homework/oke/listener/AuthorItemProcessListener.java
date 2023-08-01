package ru.otus.spring.homework.oke.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.model.document.AuthorDocument;
import ru.otus.spring.homework.oke.model.entity.Author;
import ru.otus.spring.homework.oke.service.KeyValueCacheService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorItemProcessListener implements ItemProcessListener<Author, AuthorDocument> {
    private final KeyValueCacheService cacheService;

    private final AuthorMapper authorMapper;

    @Override
    public void afterProcess(@NonNull Author author, AuthorDocument processedAuthor) {
        String generatedAuthorIdKey = this.authorMapper.mapToCacheKey(author);
        assert processedAuthor != null;
        this.cacheService.putString(generatedAuthorIdKey, processedAuthor.getId());
        log.debug(String.format("Обработан автор. Данные по идентификаторам помещены в кэш. Исходный идентификатор: " +
                "%s, сгенерированный идентификатор: %s", author.getId(), processedAuthor.getId()));
    }
}
