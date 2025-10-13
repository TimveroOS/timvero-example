alter table if exists credit_product
    add column state varchar(255);

update credit_product
set state = 'ACTIVE'
where active = true;

update credit_product
set state = 'INACTIVE'
where active = false;

alter table if exists credit_product
    alter column state set not null;

alter table if exists credit_product
    drop column if exists active;

alter table if exists credit_product
    add column copied_from UUID;

alter table if exists credit_product
    add constraint fk_credit_product_copied_from
        foreign key (copied_from) references credit_product(id);

create index idx_credit_product_copied_from on credit_product(copied_from);