
    alter table if exists entity_note 
       add column edited boolean not null;

    create table entity_note_change (
        id uuid not null,
        created_at timestamp(6) with time zone not null,
        text text,
        created_by uuid,
        note_id uuid not null,
        primary key (id)
    );

    create index idx_entity_note_owner 
       on entity_note (owner_id);

    create index idx_entity_note_change_note 
       on entity_note_change (note_id);

    alter table if exists entity_note_change 
       add constraint FKe8tfs26yuyirwds2ejj7cui4l 
       foreign key (created_by) 
       references auditor;

    alter table if exists entity_note_change 
       add constraint FKr57ligp86bgpyhcd8mc5tgbbx 
       foreign key (note_id) 
       references entity_note;
