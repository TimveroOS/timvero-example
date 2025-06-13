-- Reference
    alter table if exists notification 
       rename column notification_recipient to notification_recipient_reference;

    alter table if exists notification 
       add column notification_recipient uuid;

    alter table if exists notification 
       rename column notification_source to notification_source_reference;

    alter table if exists notification 
       add column notification_source uuid;

-- TODO migrate references