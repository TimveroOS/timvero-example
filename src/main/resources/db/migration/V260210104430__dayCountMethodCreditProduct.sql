
    alter table if exists credit_product
       add column day_count_method varchar(255) not null default '30_360_BB';

    alter table if exists credit_condition
       drop column if exists day_count_method;
