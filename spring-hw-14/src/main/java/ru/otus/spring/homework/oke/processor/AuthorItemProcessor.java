package ru.otus.spring.homework.oke.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.model.document.AuthorDocument;
import ru.otus.spring.homework.oke.model.entity.Author;

@Component
@RequiredArgsConstructor
public class AuthorItemProcessor implements ItemProcessor<Author, AuthorDocument> {
    private final AuthorMapper authorMapper;

    @Override
    public AuthorDocument process(@NonNull Author author) {
        return this.authorMapper.mapToAuthorDocumentWithGeneratedId(author);
    }
}
