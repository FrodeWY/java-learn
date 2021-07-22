create schema geek collate utf8mb4_0900_ai_ci;

create table person
(
    id      bigint auto_increment comment '主键
'
        primary key,
    name    varchar(50)  null comment '姓名',
    address varchar(200) null comment '地址',
    age     int          not null comment '年龄'
)
    comment '人员表';

INSERT INTO geek.person (id, name, address, age)
VALUES (1, '李磊', '南京鼓楼区', 12);
INSERT INTO geek.person (id, name, address, age)
VALUES (2, '莉莉', '南京建邺区', 20);
INSERT INTO geek.person (id, name, address, age)
VALUES (3, '李芳', '南京江北区', 14);