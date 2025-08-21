
    alter table if exists aud_entity_document 
       add column details varchar(255);

    alter table if exists entity_document 
       add column details varchar(255);
