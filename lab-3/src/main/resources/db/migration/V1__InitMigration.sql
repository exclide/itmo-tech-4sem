create table car_manufacturer
(
    id   bigint not null
        primary key,
    name text   not null,
    date date   not null
);

alter table car_manufacturer
    owner to postgres;

create table car_model
(
    id              bigint  not null
        primary key,
    name            text    not null,
    length          integer not null,
    width           integer not null,
    body_style      text    not null
        constraint car_model_body_style_check
            check (body_style = ANY
                   (ARRAY ['sedan'::text, 'hatchback'::text, 'coupe'::text, 'pickup'::text, 'roadster'::text])),
    manufacturer_id bigint  not null
        references car_manufacturer
            on delete cascade
);

alter table car_model
    owner to postgres;