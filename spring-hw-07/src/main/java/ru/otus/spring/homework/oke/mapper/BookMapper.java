package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book mapToBook(BookRequestDto bookRequestDto, Author author, Collection<Genre> genres) {
        Book book = new Book();
        book.setId(bookRequestDto.getId());
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(author);
        book.setGenres(genres.stream().collect(Collectors.toSet()));
        return book;
    }

    /**
     * Метод объединяет данные существующей книги с данными из dto, а также новым автором и новой коллекцией жанров
     * Объединение жанров книги выполняется по следующему принципу:
     * - удалить все жанры в существующей книге, которых нет в новой коллекции жанров
     * - добавить к жанрам существующей книги все жанры, которых в ней ещё не было
     * В силу отсутствия методов equals и hashcode у класса Genre, дополнение\удаление выпоняется при помощи стримов.
     * <p>
     * Метод меняет переданный объект book
     *
     * @param book существующая в книга
     * @param bookRequestDto запрос на изменение книги с базовой информацией о ней
     * @param newAuthor новый автор книги (должен существовать)
     * @param newGenres новая коллекция жанров книги (должны существовать)
     * @return измененный в процессе работы аргумент book со всеми обновленными полями
     */
    public Book mergeBookInfo(Book book, BookRequestDto bookRequestDto, Author newAuthor,
                              Collection<Genre> newGenres) {
        book.setId(bookRequestDto.getId());
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(newAuthor);
        Set<Genre> bookGenres = book.getGenres();
        Set<Long> oldGenreIds = bookGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        Set<Long> newGenreIds = newGenres.stream().map(Genre::getId).collect(Collectors.toSet());
        bookGenres.removeIf(genre -> !newGenreIds.contains(genre.getId()));
        newGenres.stream()
                .filter(newGenre -> !oldGenreIds.contains(newGenre.getId()))
                .forEach(newGenre -> bookGenres.add(newGenre));
        return book;
    }

    public BookResponseDto mapToBookResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        Author author = book.getAuthor();
        AuthorResponseDto authorDto = authorMapper.mapToAuthorResponseDto(author);
        dto.setAuthor(authorDto);
        Set<Genre> genres = book.getGenres();
        dto.setGenres(genres.stream().map(genreMapper::mapToGenreResponseDto).collect(Collectors.toSet()));
        return dto;
    }
}
