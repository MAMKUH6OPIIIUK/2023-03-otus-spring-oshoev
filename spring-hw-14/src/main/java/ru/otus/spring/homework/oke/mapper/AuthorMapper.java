package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.document.AuthorDocument;
import ru.otus.spring.homework.oke.model.entity.Author;
import ru.otus.spring.homework.oke.service.KeyValueCacheService;

@Component
@RequiredArgsConstructor
public class AuthorMapper {

    private final KeyValueCacheService cacheService;

    public AuthorDocument mapToAuthorDocumentWithGeneratedId(Author author) {
        AuthorDocument mappedAuthor = this.mapToAuthorWithoutId(author);
        String newAuthorId = ObjectId.get().toHexString();
        mappedAuthor.setId(newAuthorId);
        return mappedAuthor;
    }

    public AuthorDocument mapToAuthorDocumentWithCachedId(Author author) {
        AuthorDocument mappedAuthor = this.mapToAuthorWithoutId(author);
        String authorIdKey = mapToCacheKey(author);
        String cachedAuthorId = this.cacheService.getString(authorIdKey);
        mappedAuthor.setId(cachedAuthorId);
        return mappedAuthor;
    }

    public String mapToCacheKey(Author author) {
        return author.getClass().getSimpleName() + author.getId();
    }

    private AuthorDocument mapToAuthorWithoutId(Author author) {
        AuthorDocument processedAuthor = new AuthorDocument();
        processedAuthor.setName(author.getName());
        processedAuthor.setMiddleName(author.getMiddleName());
        processedAuthor.setPatronymic(author.getPatronymic());
        processedAuthor.setSurname(author.getSurname());
        return processedAuthor;
    }
}
