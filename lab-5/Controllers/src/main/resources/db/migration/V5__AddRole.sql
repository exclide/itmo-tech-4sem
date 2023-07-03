create table if not exists user_role
(
    role_id bigint not null primary key,
    name text not null
);

create table if not exists users_roles
(
    user_id bigint not null,
    role_id bigint not null
)