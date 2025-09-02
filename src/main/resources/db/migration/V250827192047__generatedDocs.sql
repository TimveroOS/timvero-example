
    create table entity_signable_document_generated (
        signable_document_id uuid not null,
        generated_documents_id uuid not null,
        generated_documents_media_type varchar(255) not null,
        primary key (signable_document_id, generated_documents_media_type)
    );

    alter table if exists entity_signable_document_generated 
       drop constraint if exists UK7ffb0h0elkeg9hkit1g4ydhcs;

    alter table if exists entity_signable_document_generated 
       add constraint UK7ffb0h0elkeg9hkit1g4ydhcs unique (generated_documents_id);

    alter table if exists entity_signable_document_generated 
       add constraint FKs9yciasadrkk9p9j414q507lc 
       foreign key (generated_documents_id) 
       references document;

    alter table if exists entity_signable_document_generated 
       add constraint FKmydjrr5bg9s6jypkekb0bt70i 
       foreign key (signable_document_id) 
       references entity_signable_document;

    insert into entity_signable_document_generated (signable_document_id, generated_documents_id, generated_documents_media_type)
    (select sd.id, d.id, TRIM(SPLIT_PART(d.content_type, ';', 1)) from 
        entity_signable_document sd
        join document d on sd.generated_document_id = d.id
    );