
    alter table if exists secured_offer 
       drop column if exists min_amount;

    alter table if exists secured_offer 
       drop column if exists max_amount;

    alter table if exists secured_offer 
       drop column if exists min_term;

    alter table if exists secured_offer 
       drop column if exists max_term;

    alter table if exists secured_offer 
       drop column if exists application_id;

    alter table if exists application 
       add column condition_id uuid;

    alter table if exists application 
       add column payment_schedule_id bigint;

    alter table if exists aud_application 
       add column condition_id uuid;

    alter table if exists aud_application 
       add column payment_schedule_id bigint;

    alter table if exists credit_condition 
       add column secured_offer_id bigint;

    alter table if exists application 
       drop constraint if exists UK8d6pfh9l2aoduehbsobwdc627;

    alter table if exists application 
       add constraint UK8d6pfh9l2aoduehbsobwdc627 unique (condition_id);

    alter table if exists application 
       drop constraint if exists UK8i7iwrjvgqsm0v6ke5aqxrxab;

    alter table if exists application 
       add constraint UK8i7iwrjvgqsm0v6ke5aqxrxab unique (payment_schedule_id);

    alter table if exists credit_condition 
       drop constraint if exists UKlp92tese8ppmrqqjfnpi9y6yl;

    alter table if exists credit_condition 
       add constraint UKlp92tese8ppmrqqjfnpi9y6yl unique (secured_offer_id);

    alter table if exists application 
       add constraint FKblau8dasp8cawuduv74fl1oqj 
       foreign key (condition_id) 
       references credit_condition;

    alter table if exists application 
       add constraint FK4his3gpc3ismkdr4eqtm17df1 
       foreign key (payment_schedule_id) 
       references credit_payment_schedule;

    alter table if exists credit_condition 
       add constraint FK7geisuq53bj5qva966fh30xqs 
       foreign key (secured_offer_id) 
       references secured_offer;
