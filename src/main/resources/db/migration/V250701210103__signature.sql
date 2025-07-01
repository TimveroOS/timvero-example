
    create table docusign_document_signature (
        envelope_id varchar(255),
        id uuid not null,
        primary key (id)
    );

    create table physical_document_signature (
        id uuid not null,
        primary key (id)
    );

    alter table if exists docusign_document_signature 
       add constraint FK18q7c8c3nq2qocbhu6kywj03y 
       foreign key (id) 
       references entity_document_signature;

    alter table if exists physical_document_signature 
       add constraint FK8ujpaq9cifpius2r9nae2iply 
       foreign key (id) 
       references entity_document_signature;
