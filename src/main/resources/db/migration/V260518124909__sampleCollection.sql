
    create table participant_response_sample (
        id bigint not null,
        participant_id uuid not null,
        primary key (id)
    );

    alter table if exists participant_response_sample 
       add constraint FKfbmqn49vy47scje50w5ej1p4x 
       foreign key (participant_id) 
       references participant;

    alter table if exists participant_response_sample 
       add constraint FKt41yqxhe78xuk2ivp6mqq5dh0 
       foreign key (id) 
       references response_sample;
