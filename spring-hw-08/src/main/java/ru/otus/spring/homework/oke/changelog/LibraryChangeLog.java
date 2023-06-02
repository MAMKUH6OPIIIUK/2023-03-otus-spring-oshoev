package ru.otus.spring.homework.oke.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

import java.util.List;

@ChangeLog(order = "001")
public class LibraryChangeLog {
    private Author tolkien;

    private Author rapov;

    private Book lordOfTheRing1;

    private Book dawnsOverRussia;

    @ChangeSet(order = "000", id = "dropDB", author = "k.oshoev")
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "k.oshoev")
    public void initAuthors(AuthorRepository repository) {
        Author author1 = new Author("Джон", "Рональд Руэл", null, "Толкин");
        this.tolkien = repository.save(author1);
        Author author2 = new Author("Михаил", null, "Александрович", "Рапов");
        this.rapov = repository.save(author2);
    }

    @ChangeSet(order = "002", id = "initBooks", author = "k.oshoev")
    public void initBooks(BookRepository repository) {
        Genre fantasy = new Genre("Фэнтези");
        Genre adventure = new Genre("Приключенческая художественная литература");
        Genre historicalProse = new Genre("Историческая проза");
        List<Genre> lotrGenres = List.of(fantasy, adventure);
        String lotrAnnotation1 = this.getLordOfTheRings1Annotation();
        Book lotr1 = new Book("Властелин колец. Братство кольца", lotrAnnotation1, this.tolkien, lotrGenres);
        this.lordOfTheRing1 = repository.save(lotr1);
        String lotrAnnotation2 = this.getLordOfTheRings2Annotation();
        Book lotr2 = new Book("Властелин колец. Две крепости", lotrAnnotation2, this.tolkien, lotrGenres);
        repository.save(lotr2);
        String lotrAnnotation3 = this.getLordOfTheRings3Annotation();
        Book lotr3 = new Book("Властелин колец. Возвращение короля", lotrAnnotation3, this.tolkien, lotrGenres);
        repository.save(lotr3);
        List<Genre> dawnsGenres = List.of(adventure, historicalProse);
        String dawnsAnnotation = this.getDawnsOverRussiaAnnotation();
        Book dawns = new Book("Зори над Русью", dawnsAnnotation, this.rapov, dawnsGenres);
        this.dawnsOverRussia = repository.save(dawns);
    }

    @ChangeSet(order = "003", id = "initComments", author = "k.oshoev")
    public void initComments(CommentRepository repository) {
        Comment lotr1Comment1 = new Comment("Интересная книга!", this.lordOfTheRing1);
        repository.save(lotr1Comment1);
        Comment lotr1Comment2 = new Comment("Кстати, экранизация почти дословно её повторяет!",
                this.lordOfTheRing1);
        repository.save(lotr1Comment2);
        Comment dawnsComment = new Comment("Захватывающее произведение", this.dawnsOverRussia);
        repository.save(dawnsComment);
    }

    private String getLordOfTheRings1Annotation() {
        return "Хоббиту Фродо, племяннику знаменитого Бильбо Бэггинса, доверена важная и очень " +
                "опасная миссия — хранить Кольцо Всевластья, которое нужно уничтожить в горниле Огненной Горы, так " +
                "как, если оно не будет уничтожено, с его помощью Тёмный Властелин Саурон сможет подчинить себе все " +
                "народы Средиземья. И отважный хоббит с друзьями отправляется в полное смертельных опасностей " +
                "путешествие…";
    }

    private String getLordOfTheRings2Annotation() {
        return "Братство распалось, но Кольцо Всевластья должно быть уничтожено. Фродо и Сэм " +
                "вынуждены довериться Голлуму, который взялся провести их к вратам Мордора. Громадная армия Сарумана " +
                "приближается: члены братства и их союзники готовы принять бой. Битва за Средиземье продолжается.";
    }

    private String getLordOfTheRings3Annotation() {
        return "Минас Тирит осажден полчищами орков. Арагорну и другим членам отряда Хранителей " +
                "необходимо не только отбить столицу Гондора, но и отвлечь Саурона на себя, чтобы Фродо и Сэм смогли " +
                "уничтожить Кольцо Всевластия. Но им приходиться только гадать — жив ли Главный Хранитель или Кольцо " +
                "Всевластия уже у Саурона?";
    }

    private String getDawnsOverRussiaAnnotation() {
        return "«Зори над Русью» — широкое историческое полотно, охватывающее период с 1359 по " +
                "1380 год, когда под гнетом татаро–монгольского ига русский народ начинает сплачиваться и собираться " +
                "с силами и наносит сокрушительный удар Золотой Орде в битве на поле Куликовом. Хотя в романе речь " +
                "идет о далеких от наших дней временах, но глубокая патриотичность повествования не может не " +
                "взволновать современного читателя.";
    }

}
