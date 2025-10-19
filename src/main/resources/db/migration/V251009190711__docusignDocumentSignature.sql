
    alter table if exists docusign_document_signature 
       add column completed_date_time timestamp(6) with time zone;

    alter table if exists docusign_document_signature 
       add column form_data jsonb;
