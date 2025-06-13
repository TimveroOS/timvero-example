    create table entity_note (
        id uuid not null,
        created_at timestamp(6) with time zone not null,
        owner_id uuid not null,
        important boolean not null,
        text text,
        created_by uuid,
        primary key (id)
    );
    
    alter table if exists entity_note 
       add constraint FKccy92wxpj6p4fthdw241e06uq 
       foreign key (created_by) 
       references auditor;