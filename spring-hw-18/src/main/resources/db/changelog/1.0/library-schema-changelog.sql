--liquibase formatted sql

--
--changeset k.oshoev:1
CREATE TABLE IF NOT EXISTS genres
(
    genre_id bigserial NOT NULL,
    name character varying(500) NOT NULL UNIQUE,
    CONSTRAINT genre_pkey PRIMARY KEY (genre_id)
);
COMMENT ON TABLE genres
    IS 'Таблица жанров';

CREATE TABLE IF NOT EXISTS authors
(
    id bigserial NOT NULL,
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
    id bigserial NOT NULL,
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
        REFERENCES genres (genre_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
COMMENT ON TABLE books_genres
    IS 'Таблица для связи книг и жанров';


CREATE TABLE IF NOT EXISTS comments
(
    id bigserial NOT NULL,
    text character varying(1000) NOT NULL,
    book_id bigint NOT NULL,
    created_on TIMESTAMP NOT NULL,
    CONSTRAINT comment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_comments_book FOREIGN KEY (book_id)
        REFERENCES books (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
COMMENT ON TABLE genres
    IS 'Таблица жанров';