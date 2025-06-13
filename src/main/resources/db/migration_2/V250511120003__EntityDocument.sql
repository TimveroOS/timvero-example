
    alter table if exists aud_entity_document 
       add column active boolean;

    alter table if exists entity_document 
       add column owner_id uuid not null;

    alter table if exists entity_signable_document 
       add column owner_id uuid not null;

    alter table if exists entity_signable_document 
       drop constraint if exists UK7j1381m7kkde6rh06n0anuir9;

    alter table if exists entity_signable_document 
       add constraint UK7j1381m7kkde6rh06n0anuir9 unique (owner_id, document_type);