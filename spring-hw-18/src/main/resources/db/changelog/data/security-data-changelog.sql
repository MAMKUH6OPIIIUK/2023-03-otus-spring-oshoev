--liquibase formatted sql

--
--changeset k.oshoev:1
insert into users(user_id, login, password, enabled) values (1, 'admin', '{UIb2+jsw61Pl8qy63B0gkSk9fMAaH0mYlBNrXHbSY74=}b5c28313d02d89b0d83b55d72f7925d7', true);
insert into users(user_id, login, password, enabled) values (2, 'user1', '{UIb2+jsw61Pl8qy63B0gkSk9fMAaH0mYlBNrXHbSY74=}b5c28313d02d89b0d83b55d72f7925d7', true);
insert into users(user_id, login, password, enabled) values (3, 'user2', '{UIb2+jsw61Pl8qy63B0gkSk9fMAaH0mYlBNrXHbSY74=}b5c28313d02d89b0d83b55d72f7925d7', true);
insert into users(user_id, login, password, enabled) values (4, 'monitor_user', '{UIb2+jsw61Pl8qy63B0gkSk9fMAaH0mYlBNrXHbSY74=}b5c28313d02d89b0d83b55d72f7925d7', true);
alter sequence users_user_id_seq restart with 5;
insert into authorities(user_id, authority) values (1, 'ROLE_ADMIN');
insert into authorities(user_id, authority) values (2, 'ROLE_USER');
insert into authorities(user_id, authority) values (3, 'ROLE_USER');
insert into authorities(user_id, authority) values (4, 'ROLE_MONITORING');
INSERT INTO acl_sid (id, principal, sid) VALUES
(1, true, 'user1'),
(2, true, 'user2'),
(3, true, 'ROLE_ADMIN');
alter sequence acl_sid_id_seq restart with 4;
INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.spring.homework.oke.dto.CommentRequestDto');
alter sequence acl_class_id_seq restart with 2;