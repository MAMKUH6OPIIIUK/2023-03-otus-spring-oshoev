package ru.otus.spring.homework.oke.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "comments")
public class CommentDocument {
    @Id
    private String id;

    private String text;

    @DBRef
    private BookDocument book;

    public CommentDocument(String text, BookDocument book) {
        this.text = text;
        this.book = book;
    }
}
