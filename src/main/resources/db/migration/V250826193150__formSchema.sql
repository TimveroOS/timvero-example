
    alter table if exists aud_form_schema 
       drop constraint if exists aud_form_schema_pkey;   

    alter table if exists aud_form_schema 
       add column id uuid;
       
    update aud_form_schema set id = export_id;
    
    update aud_form_schema set id = gen_random_uuid() where id is null;
    
    alter table if exists aud_form_schema 
       alter column id set not null;
       
    alter table if exists aud_form_schema 
       drop column export_id;

    alter table if exists aud_form_schema 
       add primary key (rev, id);
       
       
    alter table if exists form_schema 
       add column id uuid;
       
    update form_schema set id = export_id;
    
    update form_schema set id = gen_random_uuid() where id is null;


    alter table if exists aud_pending_decision 
       add column schema_id uuid;
       
    update aud_pending_decision set schema_id = (select fs.id from form_schema fs where fs.code = schema_code);
    
    alter table if exists aud_pending_decision 
       drop column schema_code;
    

    alter table if exists decision_fact_fixed 
       add column schema_id uuid;
       
    update decision_fact_fixed set schema_id = (select fs.id from form_schema fs where fs.code = schema_code);
    
    alter table if exists decision_fact_fixed 
       drop column schema_code;
       

    alter table if exists pending_decision 
       add column schema_id uuid;
       
    update pending_decision set schema_id = (select fs.id from form_schema fs where fs.code = schema_code);
    
    alter table if exists pending_decision 
       drop column schema_code;

       
    alter table if exists form_schema 
       alter column id set not null;
       
    alter table if exists form_schema 
       drop column export_id;
       
    alter table if exists form_schema 
       drop constraint if exists form_schema_pkey;
       
    alter table if exists form_schema 
       add primary key (id);
       

    alter table if exists form_schema 
       drop constraint if exists UK_form_schema_code;

    alter table if exists form_schema 
       add constraint UK_form_schema_code unique (code);

    alter table if exists decision_fact_fixed 
       add constraint FK3nenstkcexqihisc61xf160sc 
       foreign key (schema_id) 
       references form_schema;

    alter table if exists pending_decision 
       add constraint FK73koe5o2x6n9jupw139g1kp9u 
       foreign key (schema_id) 
       references form_schema;
