
    alter table if exists aud_participant 
       add column github_username varchar(255);

    create table aud_participant_roles (
        rev integer not null,
        participant_id uuid not null,
        roles varchar(255) not null,
        revtype smallint,
        primary key (participant_id, rev, roles)
    );

    alter table if exists participant 
       add column github_username varchar(255);

    create table participant_roles (
        participant_id uuid not null,
        roles varchar(255)
    );

    alter table if exists aud_participant_roles 
       add constraint FKi6mycihqnw3w0rq5cah87lnr 
       foreign key (rev) 
       references aud;

    alter table if exists participant_roles 
       add constraint FK1i0v4ct8odbbrbyokvhmqa2sf 
       foreign key (participant_id) 
       references participant;
