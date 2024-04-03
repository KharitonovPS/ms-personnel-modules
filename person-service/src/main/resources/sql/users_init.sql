drop table if exists users;
drop table if exists roles;
drop table if exists user_role;
create table users
(
    id   bigserial PRIMARY KEY,
    name varchar(24) not null
);

create table roles
(
    id          bigserial PRIMARY KEY,
    name        varchar(24) not null,
    description varchar(24)
);

create table user_role
(
    id      bigserial PRIMARY KEY,
    user_id bigint,
    role_id bigint,
    foreign key (user_id) references users (id) on DELETE cascade,
    foreign key (role_id) references roles (id) on delete cascade
);
insert into users
values (1,'vasya'),
       (2,'petya');
insert into roles
values (1,'read'),
       (2,'write');
insert into user_role values (1, 1, 1);

insert into user_role values (2, 1, 2);

insert into user_role
values (3, 2, 1);

delete from user_role where id = 1;

delete from roles where id = 2;

delete from users where id = 1;




