package ru.otus.spring.homework.oke.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.homework.oke.changelog.test.data.DataGenerator;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

@ChangeLog(order = "001")
public class LibraryChangeLog {
    @ChangeSet(order = "000", id = "dropDB", author = "k.oshoev", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "k.oshoev", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        Author author1 = DataGenerator.getFirstAuthor();
        repository.save(author1);
        Author author2 = DataGenerator.getSecondAuthor();
        repository.save(author2);
        Author author3 = DataGenerator.getThirdAuthor();
        repository.save(author3);
    }

    @ChangeSet(order = "002", id = "initBooks", author = "k.oshoev", runAlways = true)
    public void initBooks(BookRepository repository) {
        Book book1 = DataGenerator.getFirstBook();
        repository.save(book1);
        Book book2 = DataGenerator.getSecondBook();
        repository.save(book2);
        Book book3 = DataGenerator.getThirdBook();
        repository.save(book3);
    }

    @ChangeSet(order = "003", id = "initComments", author = "k.oshoev", runAlways = true)
    public void initComments(CommentRepository repository) {
        Comment book1Comment1 = DataGenerator.getFirstComment();
        repository.save(book1Comment1);
        Comment book1Comment2 = DataGenerator.getSecondComment();
        repository.save(book1Comment2);
        Comment book2Comment1 = DataGenerator.getThirdComment();
        repository.save(book2Comment1);
    }

}
