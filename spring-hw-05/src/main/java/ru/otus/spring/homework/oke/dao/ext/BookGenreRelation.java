package ru.otus.spring.homework.oke.dao.ext;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookGenreRelation {
    private final long bookId;

    private final long genreId;
}
