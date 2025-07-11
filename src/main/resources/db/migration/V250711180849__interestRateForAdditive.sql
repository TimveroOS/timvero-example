
alter table if exists credit_product_additive
    add column interest_rate numeric(19,2) not null default 5;

alter table if exists credit_product_additive
    alter column interest_rate drop default ;
