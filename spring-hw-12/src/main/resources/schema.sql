CREATE TABLE IF NOT EXISTS users
(
    username varchar_ignorecase(50) NOT NULL PRIMARY KEY,
    password varchar_ignorecase(500) NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities
(
    username varchar_ignorecase(50) NOT NULL,
    authority varchar_ignorecase(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username)
        REFERENCES users(username)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username on authorities (username,authority);


CREATE TABLE IF NOT EXISTS genres
(
    id bigint NOT NULL AUTO_INCREMENT,
    name character varying(500) NOT NULL UNIQUE,
    CONSTRAINT genre_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE genres
    IS 'Таблица жанров';

CREATE TABLE IF NOT EXISTS authors
(
    id bigint NOT NULL AUTO_INCREMENT,
    name character varying(255) NOT NULL,
    middle_name character varying(255),
    patronymic character varying(255),
    surname character varying(255) NOT NULL,
    CONSTRAINT author_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE authors
    IS 'Таблица авторов';

CREATE TABLE IF NOT EXISTS books
(
    id bigint NOT NULL AUTO_INCREMENT,
    title character varying(500) NOT NULL,
    description character varying(1000),
    author_id bigint NOT NULL,
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT fk_books_authors FOREIGN KEY (author_id)
            REFERENCES authors (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);
COMMENT ON TABLE books
    IS 'Таблица книг';

CREATE TABLE IF NOT EXISTS books_genres
(
    book_id bigint NOT NULL,
    genre_id bigint NOT NULL,
    CONSTRAINT book_genre_pkey PRIMARY KEY (book_id, genre_id),
    CONSTRAINT fk_books_genres_book FOREIGN KEY (book_id)
        REFERENCES books (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_books_genres_genre FOREIGN KEY (genre_id)
        REFERENCES genres (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
COMMENT ON TABLE books_genres
    IS 'Таблица для связи книг и жанров';


CREATE TABLE IF NOT EXISTS comments
(
    id bigint NOT NULL AUTO_INCREMENT,
    text character varying(1000) NOT NULL,
    book_id bigint NOT NULL,
    CONSTRAINT comment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_comments_book FOREIGN KEY (book_id)
        REFERENCES books (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
COMMENT ON TABLE genres
    IS 'Таблица жанров';