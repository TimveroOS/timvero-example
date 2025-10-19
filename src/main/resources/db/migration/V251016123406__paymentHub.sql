
    alter table if exists aud_credit_operation 
       add column credit_payment_type varchar(255);

    alter table if exists aud_payment_transaction 
       add column date date;

    alter table if exists aud_payment_transaction 
       add column owner_id uuid;

    alter table if exists aud_payment_transaction 
       add column purpose varchar(255);

    alter table if exists credit_operation 
       add column credit_payment_type varchar(255);

    alter table if exists credit_operation 
       add column payment_transaction_id bigint;

    alter table if exists credit_product 
       add column copied_from uuid;

    alter table if exists credit_product 
       add column state varchar(255) not null;

    alter table if exists payment_transaction 
       add column date date not null;

    alter table if exists payment_transaction 
       add column owner_id uuid;

    alter table if exists payment_transaction 
       add column purpose varchar(255);

    alter table if exists credit_operation 
       add constraint FKlfcp2p9d31mw6vash0kd5kfmx 
       foreign key (payment_transaction_id) 
       references payment_transaction;
