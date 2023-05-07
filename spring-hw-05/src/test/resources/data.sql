insert into authors(id, name, middle_name, patronymic, surname) values (1, 'Имя1', 'Среднее имя1', null, 'Фамилия1');
insert into authors(id, name, middle_name, patronymic, surname) values (2, 'Имя2', null, 'Отчество2', 'Фамилия2');
insert into authors(id, name, middle_name, patronymic, surname) values (3, 'Имя3', null, 'Отчество3', 'Фамилия3');
alter table authors alter column id restart with 4;

insert into genres(id, name) values (1, 'Жанр1');
insert into genres(id, name) values (2, 'Жанр2');
insert into genres(id, name) values (3, 'Жанр3');
alter table genres alter column id restart with 4;

insert into books(id, title, description, author_id)
values (1,
        'Книга1',
        'Описание1',
        1);
insert into books(id, title, description, author_id)
values (2,
        'Книга2',
        'Описание2',
        1);
insert into books(id, title, description, author_id)
values (3,
        'Книга3',
        'Описание3',
        2);
alter table books alter column id restart with 4;

insert into books_genres(book_id, genre_id) values (1,1);
insert into books_genres(book_id, genre_id) values (1,2);
insert into books_genres(book_id, genre_id) values (2,1);
insert into books_genres(book_id, genre_id) values (2,2);
insert into books_genres(book_id, genre_id) values (3,2);