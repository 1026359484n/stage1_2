create table if not exists `order`
(
    id         int auto_increment
        primary key,
    order_time datetime null,
    total      double   null,
    uid        int      null
);

create table if not exists role
(
    id        int auto_increment
        primary key,
    role_name varchar(50) null
);

create table if not exists user
(
    id       int auto_increment
        primary key,
    username varchar(50) null,
    password varchar(50) null,
    birthday datetime    null
);

create table if not exists user_role
(
    user_id int not null,
    role_id int not null,
    constraint user_role_user_id_role_id_uindex
        unique (user_id, role_id)
);



INSERT INTO test_mybatis.`order` (id, order_time, total, uid) VALUES (1, '2021-06-01 19:19:58', 123.42, 1);
INSERT INTO test_mybatis.`order` (id, order_time, total, uid) VALUES (2, '2021-06-01 19:23:05', 4000, 1);
INSERT INTO test_mybatis.`order` (id, order_time, total, uid) VALUES (3, '2021-06-01 19:23:19', 5000, 3);


INSERT INTO test_mybatis.user (id, username, password) VALUES (1, 'root', 'asdfpon');
INSERT INTO test_mybatis.user (id, username, password) VALUES (3, 'oijsdf', '13551');
INSERT INTO test_mybatis.user (id, username, password) VALUES (6, 'asdf', 'asdf');