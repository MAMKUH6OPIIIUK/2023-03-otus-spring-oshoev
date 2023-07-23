CREATE TABLE IF NOT EXISTS users
(
    user_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    login varchar_ignorecase(50) NOT NULL UNIQUE,
    password varchar_ignorecase(500) NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities
(
    user_id bigint NOT NULL,
    authority varchar_ignorecase(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username on authorities (user_id,authority);

CREATE TABLE IF NOT EXISTS acl_sid (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  principal tinyint NOT NULL,
  sid varchar(100) NOT NULL,
  CONSTRAINT unique_uk_1 UNIQUE (sid, principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  class varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  acl_object_identity bigint NOT NULL,
  ace_order int NOT NULL,
  sid bigint NOT NULL,
  mask int NOT NULL,
  granting tinyint NOT NULL,
  audit_success tinyint NOT NULL,
  audit_failure tinyint NOT NULL,
  CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity,ace_order)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  object_id_class bigint NOT NULL,
  object_id_identity bigint NOT NULL,
  parent_object bigint DEFAULT NULL,
  owner_sid bigint DEFAULT NULL,
  entries_inheriting tinyint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_uk_3 UNIQUE (object_id_class,object_id_identity)
);

ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id bigint NOT NULL AUTO_INCREMENT,
    name character varying(500) NOT NULL UNIQUE,
    CONSTRAINT genre_pkey PRIMARY KEY (genre_id)
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
        REFERENCES genres (genre_id)
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