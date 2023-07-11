package ru.otus.spring.homework.oke.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String text;

    @Indexed
    private String bookId;

    public Comment(String text, String bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
