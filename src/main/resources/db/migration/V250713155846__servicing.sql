
    alter table if exists aud_credit_operation 
       add column maturity boolean;

    alter table if exists aud_credit_operation 
       add column condition_id uuid;

    alter table if exists credit 
       add column application_id uuid not null;

    alter table if exists credit 
       add column condition uuid;

    alter table if exists credit_condition 
       add column day_count_method varchar(255) not null;

    alter table if exists credit_condition 
       rename column annual_interest to interest_rate;

    alter table if exists credit_condition 
       add column late_fee_rate numeric not null;

    alter table if exists credit_condition 
       add column regular_payment_currency varchar(3);

    alter table if exists credit_condition 
       add column regular_payment_number numeric(19,2);

    alter table if exists credit_operation 
       add column maturity boolean;

    alter table if exists credit_operation 
       add column condition_id uuid;

    alter table if exists credit_product 
       add column late_fee_rate numeric not null;

    alter table if exists credit 
       drop constraint if exists UKeuqkvngp521f962617kl9fa29;

    alter table if exists credit 
       add constraint UKeuqkvngp521f962617kl9fa29 unique (application_id);

    alter table if exists credit 
       drop constraint if exists UK4rnq56phihyxxoxx64rwt04gk;

    alter table if exists credit 
       add constraint UK4rnq56phihyxxoxx64rwt04gk unique (condition);

    alter table if exists credit 
       add constraint FKnx8hcl8assdw5d7qnnk8vx3fv 
       foreign key (application_id) 
       references application;

    alter table if exists credit 
       add constraint FK3cws7axh7jpug26u7fkqcxyln 
       foreign key (condition) 
       references credit_condition;

    alter table if exists credit_operation 
       add constraint FKf88dunqjdmo0jibljrdh8m6v1 
       foreign key (condition_id) 
       references credit_condition;
