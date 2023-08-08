package ru.otus.spring.homework.oke.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "books")
public class BookDocument {
    @Id
    private String id;

    private String title;

    private String description;

    @DocumentReference
    private AuthorDocument author;

    private List<GenreDocument> genres;

    public BookDocument(String title, String description, AuthorDocument author, List<GenreDocument> genres) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.genres = genres;
    }
}
