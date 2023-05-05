alter table car_manufacturer
    add column user_id bigint not null
        references user_table default 0