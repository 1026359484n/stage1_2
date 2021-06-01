create table `order`
(
    id         int auto_increment
        primary key,
    order_time datetime null,
    total      double   null,
    uid        int      null
);

INSERT INTO test_mybatis.`order` (id, order_time, total, uid) VALUES (1, '2021-06-01 19:19:58', 123.42, 1);
INSERT INTO test_mybatis.`order` (id, order_time, total, uid) VALUES (2, '2021-06-01 19:23:05', 4000, 1);
INSERT INTO test_mybatis.`order` (id, order_time, total, uid) VALUES (3, '2021-06-01 19:23:19', 5000, 3);
create table user
(
    id       int auto_increment
        primary key,
    username varchar(50) null,
    password varchar(50) null
);

INSERT INTO test_mybatis.user (id, username, password) VALUES (1, 'root', 'asdfpon');
INSERT INTO test_mybatis.user (id, username, password) VALUES (3, 'oijsdf', '13551');
INSERT INTO test_mybatis.user (id, username, password) VALUES (6, 'asdf', 'asdf');