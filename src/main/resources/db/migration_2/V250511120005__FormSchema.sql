    create table aud_form_schema (
        code varchar(255) not null,
        rev integer not null,
        revtype smallint,
        active boolean,
        description varchar(255),
        export_id uuid default gen_random_uuid(),
        schema jsonb,
        primary key (rev, code)
    );
    
    create table form_schema (
        code varchar(255) not null,
        active boolean not null,
        description varchar(255) not null,
        export_id uuid default gen_random_uuid() not null,
        schema jsonb,
        primary key (code)
    );
    
    alter table if exists form_schema 
       drop constraint if exists UKjk790y38mhsgwrfkgjqv4iyk3;

    alter table if exists form_schema 
       add constraint UKjk790y38mhsgwrfkgjqv4iyk3 unique (export_id);

    alter table if exists aud_form_schema 
       add constraint FKql8mlpisg717ib38sq6pdl131 
       foreign key (rev) 
       references aud;