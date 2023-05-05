alter table user_table
    add column manufacturer_id bigint;

insert into user_table (user_id, username, password, manufacturer_id)
values (0,'admin','$2a$10$pmP6YjH.NshK.NmOh3yD3uKnE1NM2Vm5DitWe1lfI2MwxmcHS3ute', null),
       (1,'user','$2a$10$i6UieC/fqxE/QW1/J0DygOwd2Qi7jDGp7PjxWGMnIPhGwdhR6Pdkq', 0),
       (2,'user2','$2a$10$i6UieC/fqxE/QW1/J0DygOwd2Qi7jDGp7PjxWGMnIPhGwdhR6Pdkq', 1),
       (3,'user3','$2a$10$i6UieC/fqxE/QW1/J0DygOwd2Qi7jDGp7PjxWGMnIPhGwdhR6Pdkq', 2);

insert into car_manufacturer (id, name, date, user_id)
values (0, 'bmw', '2022-01-01', 1),
       (1, 'merc', '2023-01-01', 2),
       (2, 'xyz', '2024-01-01', 3);

insert into car_model (id, name, length, width, height, body_style, manufacturer_id)
values (0, 'bmw1', 1, 1, 1, 'sedan', 0),
       (1, 'bmw2', 1, 1, 1, 'pickup', 0),
       (2, 'merc1', 1, 1, 1, 'coupe', 1),
       (3, 'merc2', 1, 1, 1, 'sedan', 1),
       (4, 'xyz1', 1, 1, 1, 'pickup', 2),
       (5, 'xyz2', 1, 1, 1, 'sedan', 2),
       (6, 'xyz3', 1, 1, 1, 'coupe', 2);


insert into user_role (role_id, name)
values (0, 'ADMIN'), (1, 'USER');

insert into users_roles (user_id, role_id)
values (0, 0), (0, 1), (1, 1), (2, 1), (3, 1);