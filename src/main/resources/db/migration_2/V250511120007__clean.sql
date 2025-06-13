    alter table if exists aud_entity_document 
       drop column uuid;

    alter table if exists aud_entity_signable_document 
       drop column uuid;

    alter table if exists document 
       drop column updated_at;

    alter table if exists document 
       drop column updated_by;

    alter table if exists entity_document 
       drop column uuid;

    alter table if exists entity_document 
       drop column updated_at;

    alter table if exists entity_document 
       drop column updated_by;

    alter table if exists entity_document_signature 
       drop column created_at;

    alter table if exists entity_document_signature 
       drop column updated_at;

    alter table if exists entity_document_signature 
       drop column created_by;

    alter table if exists entity_document_signature 
       drop column updated_by;

    alter table if exists entity_signable_document 
       drop column uuid;

    alter table if exists entity_signable_document 
       drop column updated_at;

    alter table if exists entity_signable_document 
       drop column updated_by;

    alter table if exists user_role 
       drop column export_id;

    alter table if exists user_role 
       drop constraint if exists UK_gryx7lwyvl260hgv8ans6ms1q;

    drop table if exists editable_property; 

    drop sequence if exists address_id_seq;

    drop sequence if exists auditor_id_seq;

    drop sequence if exists document_id_seq;

    drop sequence if exists document_template_id_seq;

    drop sequence if exists editable_property_id_seq;

    drop sequence if exists entity_document_id_seq;

    drop sequence if exists entity_document_signature_id_seq;

    drop sequence if exists entity_signable_document_id_seq;

    drop sequence if exists notification_id_seq;

    drop sequence if exists notification_template_id_seq;

    drop sequence if exists user_account_id_seq;

    drop sequence if exists user_role_id_seq;


