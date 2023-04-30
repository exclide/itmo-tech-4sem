alter table if exists car_model
    add column height integer;

create table car_engine
(
    engineId bigint not null primary key,
    engineName text not null,
    volume integer not null,
    cylinders integer not null,
    height integer not null,
    carModelId bigint not null references car_model
        on delete cascade
);

alter table car_engine
    owner to postgres;