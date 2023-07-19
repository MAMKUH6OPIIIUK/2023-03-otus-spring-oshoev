insert into users(user_id, login, password, enabled) values (1, 'admin', '{UIb2+jsw61Pl8qy63B0gkSk9fMAaH0mYlBNrXHbSY74=}b5c28313d02d89b0d83b55d72f7925d7', 1);
insert into users(user_id, login, password, enabled) values (2, 'user', '{UIb2+jsw61Pl8qy63B0gkSk9fMAaH0mYlBNrXHbSY74=}b5c28313d02d89b0d83b55d72f7925d7', 1);
alter table users alter column user_id restart with 3;

insert into authorities(user_id, authority) values (1, 'USER');
insert into authorities(user_id, authority) values (1, 'ADMIN');
insert into authorities(user_id, authority) values (2, 'USER');


insert into authors(id, name, middle_name, patronymic, surname) values (1, 'Джон', 'Рональд Руэл', null, 'Толкин');
insert into authors(id, name, middle_name, patronymic, surname) values (2, 'Михаил', null, 'Александрович', 'Рапов');
alter table authors alter column id restart with 3;

insert into genres(genre_id, name) values (1, 'Фэнтези');
insert into genres(genre_id, name) values (2, 'Приключенческая художественная литература');
insert into genres(genre_id, name) values (3, 'Историческая проза');
insert into genres(genre_id, name) values (4, 'Мистика');
alter table genres alter column genre_id restart with 5;

insert into books(id, title, description, author_id)
values (1,
        'Властелин колец. Братство кольца',
        'Хоббиту Фродо, племяннику знаменитого Бильбо Бэггинса, доверена важная и очень опасная миссия — хранить Кольцо Всевластья, которое нужно уничтожить в горниле Огненной Горы, так как, если оно не будет уничтожено, с его помощью Тёмный Властелин Саурон сможет подчинить себе все народы Средиземья. И отважный хоббит с друзьями отправляется в полное смертельных опасностей путешествие…',
        1);
insert into books(id, title, description, author_id)
values (2,
        'Властелин колец. Две крепости',
        'Братство распалось, но Кольцо Всевластья должно быть уничтожено. Фродо и Сэм вынуждены довериться Голлуму, который взялся провести их к вратам Мордора. Громадная армия Сарумана приближается: члены братства и их союзники готовы принять бой. Битва за Средиземье продолжается.',
        1);
insert into books(id, title, description, author_id)
values (3,
        'Властелин колец. Возвращение короля',
        'Минас Тирит осажден полчищами орков. Арагорну и другим членам отряда Хранителей необходимо не только отбить столицу Гондора, но и отвлечь Саурона на себя, чтобы Фродо и Сэм смогли уничтожить Кольцо Всевластия. Но им приходиться только гадать — жив ли Главный Хранитель или Кольцо Всевластия уже у Саурона?',
        1);
insert into books(id, title, description, author_id)
values (4,
        'Зори над Русью',
        '«Зори над Русью» — широкое историческое полотно, охватывающее период с 1359 по 1380 год, когда под гнетом татаро–монгольского ига русский народ начинает сплачиваться и собираться с силами и наносит сокрушительный удар Золотой Орде в битве на поле Куликовом. Хотя в романе речь идет о далеких от наших дней временах, но глубокая патриотичность повествования не может не взволновать современного читателя.',
        2);
alter table books alter column id restart with 5;

insert into books_genres(book_id, genre_id) values (1,1);
insert into books_genres(book_id, genre_id) values (1,2);
insert into books_genres(book_id, genre_id) values (2,1);
insert into books_genres(book_id, genre_id) values (2,2);
insert into books_genres(book_id, genre_id) values (3,1);
insert into books_genres(book_id, genre_id) values (3,2);
insert into books_genres(book_id, genre_id) values (4,2);
insert into books_genres(book_id, genre_id) values (4,3);