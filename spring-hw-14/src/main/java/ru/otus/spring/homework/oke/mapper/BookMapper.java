package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.document.AuthorDocument;
import ru.otus.spring.homework.oke.model.document.BookDocument;
import ru.otus.spring.homework.oke.model.document.GenreDocument;
import ru.otus.spring.homework.oke.model.entity.Book;
import ru.otus.spring.homework.oke.service.KeyValueCacheService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    private final KeyValueCacheService cacheService;

    public BookDocument mapToBookDocumentWithGeneratedId(Book book) {
        BookDocument mappedBook = this.mapToBookDocumentWithoutId(book);
        String newBookId = ObjectId.get().toHexString();
        mappedBook.setId(newBookId);
        return mappedBook;
    }

    public BookDocument mapToEmptyBookDocumentWithCachedId(Long bookId) {
        BookDocument mappedBook = new BookDocument();
        String bookIdKey = this.mapToCacheKey(bookId);
        String cachedBookId = this.cacheService.getString(bookIdKey);
        mappedBook.setId(cachedBookId);
        return mappedBook;
    }

    public String mapToCacheKey(Book book) {
        return this.mapToCacheKey(book.getId());
    }

    private String mapToCacheKey(Long bookId) {
        return Book.class.getSimpleName() + bookId;
    }

    private BookDocument mapToBookDocumentWithoutId(Book book) {
        BookDocument mappedBook = new BookDocument();
        mappedBook.setTitle(book.getTitle());
        mappedBook.setDescription(book.getDescription());
        AuthorDocument authorDocument = this.authorMapper.mapToAuthorDocumentWithCachedId(book.getAuthor());
        mappedBook.setAuthor(authorDocument);
        List<GenreDocument> genreDocuments = book.getGenres().stream()
                .map(this.genreMapper::mapToGenreDocument)
                .collect(Collectors.toList());
        mappedBook.setGenres(genreDocuments);
        return mappedBook;
    }
}
