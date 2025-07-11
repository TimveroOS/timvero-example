
    alter table if exists aud_participant 
       add column offers_generated_at timestamp(6) with time zone;

    alter table if exists aud_participant 
       add column offer_generation_exception_id bigint;

    alter table if exists participant 
       add column offers_generated_at timestamp(6) with time zone;

    alter table if exists participant 
       add column offer_generation_exception_id bigint;

    alter table if exists participant 
       add constraint FKfa1x8eit2vbecr5hgknrwygss 
       foreign key (offer_generation_exception_id) 
       references exception_entity;
