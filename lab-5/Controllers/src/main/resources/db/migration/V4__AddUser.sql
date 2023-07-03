create table if not exists user_table
(
    user_id bigint not null primary key,
    username text not null,
    password text not null
);