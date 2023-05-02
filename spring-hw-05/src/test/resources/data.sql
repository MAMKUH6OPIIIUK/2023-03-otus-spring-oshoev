insert into authors(name, middle_name, patronymic, surname) values ('Имя1', 'Среднее имя1', null, 'Фамилия1');
insert into authors(name, middle_name, patronymic, surname) values ('Имя2', null, 'Отчество2', 'Фамилия2');
insert into authors(name, middle_name, patronymic, surname) values ('Имя3', null, 'Отчество3', 'Фамилия3');

insert into genres(name) values ('Жанр1');
insert into genres(name) values ('Жанр2');
insert into genres(name) values ('Жанр3');

insert into books(title, description, author_id)
values ('Книга1',
        'Описание1',
        1);
insert into books(title, description, author_id)
values ('Книга2',
        'Описание2',
        1);
insert into books(title, description, author_id)
values ('Книга3',
        'Описание3',
        2);

insert into books_genres(book_id, genre_id) values (1,1);
insert into books_genres(book_id, genre_id) values (1,2);
insert into books_genres(book_id, genre_id) values (2,1);
insert into books_genres(book_id, genre_id) values (2,2);
insert into books_genres(book_id, genre_id) values (3,2);