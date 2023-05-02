insert into authors(name, middle_name, patronymic, surname) values ('Джон', 'Рональд Руэл', null, 'Толкин');
insert into authors(name, middle_name, patronymic, surname) values ('Михаил', null, 'Александрович', 'Рапов');

insert into genres(name) values ('Фэнтези');
insert into genres(name) values ('Приключенческая художественная литература');
insert into genres(name) values ('Историческая проза');
insert into genres(name) values ('Мистика');

insert into books(title, description, author_id)
values ('Властелин колец. Братство кольца',
        'Хоббиту Фродо, племяннику знаменитого Бильбо Бэггинса, доверена важная и очень опасная миссия — хранить Кольцо Всевластья, которое нужно уничтожить в горниле Огненной Горы, так как, если оно не будет уничтожено, с его помощью Тёмный Властелин Саурон сможет подчинить себе все народы Средиземья. И отважный хоббит с друзьями отправляется в полное смертельных опасностей путешествие…',
        1);
insert into books(title, description, author_id)
values ('Властелин колец. Две крепости',
        'Братство распалось, но Кольцо Всевластья должно быть уничтожено. Фродо и Сэм вынуждены довериться Голлуму, который взялся провести их к вратам Мордора. Громадная армия Сарумана приближается: члены братства и их союзники готовы принять бой. Битва за Средиземье продолжается.',
        1);
insert into books(title, description, author_id)
values ('Властелин колец. Возвращение короля',
        'Минас Тирит осажден полчищами орков. Арагорну и другим членам отряда Хранителей необходимо не только отбить столицу Гондора, но и отвлечь Саурона на себя, чтобы Фродо и Сэм смогли уничтожить Кольцо Всевластия. Но им приходиться только гадать — жив ли Главный Хранитель или Кольцо Всевластия уже у Саурона?',
        1);
insert into books(title, description, author_id)
values ('Зори над Русью',
        '«Зори над Русью» — широкое историческое полотно, охватывающее период с 1359 по 1380 год, когда под гнетом татаро–монгольского ига русский народ начинает сплачиваться и собираться с силами и наносит сокрушительный удар Золотой Орде в битве на поле Куликовом. Хотя в романе речь идет о далеких от наших дней временах, но глубокая патриотичность повествования не может не взволновать современного читателя.',
        2);

insert into books_genres(book_id, genre_id) values (1,1);
insert into books_genres(book_id, genre_id) values (1,2);
insert into books_genres(book_id, genre_id) values (2,1);
insert into books_genres(book_id, genre_id) values (2,2);
insert into books_genres(book_id, genre_id) values (3,1);
insert into books_genres(book_id, genre_id) values (3,2);
insert into books_genres(book_id, genre_id) values (4,2);
insert into books_genres(book_id, genre_id) values (4,3);