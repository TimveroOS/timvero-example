
    create table aud_decision_department (
        id uuid not null,
        rev integer not null,
        revtype smallint,
        code varchar(255),
        description varchar(255),
        primary key (rev, id)
    );
    
    alter table if exists aud_decline_reason 
       drop constraint if exists aud_decline_reason_pkey;   

    alter table if exists aud_decline_reason 
       add column id uuid;
       
    update aud_decline_reason set id = export_id;
    
    update aud_decline_reason set id = gen_random_uuid() where id is null;
    
    alter table if exists aud_decline_reason 
       alter column id set not null;
       
    alter table if exists aud_decline_reason 
       drop column export_id;

    alter table if exists aud_decline_reason 
       add primary key (rev, id);

    create table aud_reference_amount (
        id uuid not null,
        rev integer not null,
        revtype smallint,
        date date,
        index numeric,
        currency varchar(3),
        primary key (rev, id)
    );

    create table aud_reference_rate (
        id uuid not null,
        rev integer not null,
        revtype smallint,
        date date,
        index numeric,
        abbreviation varchar(255),
        description varchar(255),
        primary key (rev, id)
    );
    
    alter table if exists decision_fact_fixed 
       drop constraint if exists fkig9v62lmbd14c6jdhvii2479b;
       
    alter table if exists pending_decision 
       drop constraint if exists fkx71mbc2nk89wtwltvnb10m0c;
       
    alter table if exists user_account_decision_department 
        drop constraint if exists  fk8ybrl26sm8lvjjv40obm1jpap;
       
    alter table if exists decision_department 
       drop constraint if exists decision_department_pkey;
       
    alter table if exists decision_department 
       add column id uuid;
       
    update decision_department set id = export_id;
    
    update decision_department set id = gen_random_uuid() where id is null;
    
    alter table if exists decision_department 
       alter column id set not null;
       
    alter table if exists decision_department 
       drop column export_id;

    alter table if exists decision_department 
       add primary key (id);
       
    alter table if exists decision_fact_fixed 
       drop constraint if exists fkhlbu9r4xcuuhpuqc22b91sduk;
       
    alter table if exists pending_decision 
       drop constraint if exists fk9ebi8la1sk3s7kfl6pfcgq2ro;
       
    alter table if exists decline_reason 
       drop constraint if exists decline_reason_pkey;       
     
    alter table if exists decline_reason 
       add column id uuid;
       
    update decline_reason set id = export_id;
    
    update decline_reason set id = gen_random_uuid() where id is null;
    
    alter table if exists decline_reason 
       alter column id set not null;
       
    alter table if exists decline_reason 
       drop column export_id;
       
    alter table if exists decline_reason 
       add primary key (id);
 
       
    alter table if exists reference_amount 
       drop constraint if exists reference_amount_pkey;
       
    alter table if exists reference_amount 
       rename column id to old_id;
    
    alter table if exists reference_amount 
       add column id uuid default gen_random_uuid() not null;
       
    alter table if exists reference_amount 
       add primary key (id);
       
       
    alter table if exists reference_rate 
       drop constraint if exists reference_rate_pkey;
       
    alter table if exists reference_rate 
       rename column id to old_id;
    
    alter table if exists reference_rate 
       add column id uuid default gen_random_uuid() not null;
       
    alter table if exists reference_rate 
       add primary key (id);

       
       
	create function pg_temp.to_decision_department_id(varchar)
	    returns uuid language sql as $$
	select id from decision_department where code = $1
	$$;
	
    create function pg_temp.to_decline_reason_id(varchar)
        returns uuid language sql as $$
    select id from decline_reason where code = $1
    $$;
       
    alter table if exists pending_decision 
       alter column decision_department set data type uuid using pg_temp.to_decision_department_id(decision_department);

    alter table if exists pending_decision 
       alter column decline_reason_id set data type uuid using pg_temp.to_decline_reason_id(decline_reason_id);
       
    alter table if exists aud_pending_decision 
       alter column decision_department set data type uuid using pg_temp.to_decision_department_id(decision_department);

    alter table if exists aud_pending_decision 
       alter column decline_reason_id set data type uuid using pg_temp.to_decline_reason_id(decline_reason_id);

    alter table if exists decision_fact_fixed 
       alter column decision_department set data type uuid using pg_temp.to_decision_department_id(decision_department);

    alter table if exists decision_fact_fixed 
       alter column decline_reason_id set data type uuid using pg_temp.to_decline_reason_id(decline_reason_id);


    alter table if exists decision_department 
       drop constraint if exists UK_decision_department_code;

    alter table if exists decision_department 
       add constraint UK_decision_department_code unique (code);

    alter table if exists decline_reason 
       drop constraint if exists UK_decline_reason_code;

    alter table if exists decline_reason 
       add constraint UK_decline_reason_code unique (code);

    alter table if exists reference_amount 
       drop constraint if exists UK_reference_amount_date;

    alter table if exists reference_amount 
       add constraint UK_reference_amount_date unique (date);

    alter table if exists reference_rate 
       drop constraint if exists UK_reference_rate_abbreviation_date;

    alter table if exists reference_rate 
       add constraint UK_reference_rate_abbreviation_date unique (abbreviation, date);

    alter table if exists aud_decision_department 
       add constraint FKgkp5o0goc23ejrk6dbqsve7yd 
       foreign key (rev) 
       references aud;

    alter table if exists aud_reference_amount 
       add constraint FKfgc38ugsxyxgy7v6bbkgsr2xy 
       foreign key (rev) 
       references aud;

    alter table if exists aud_reference_rate 
       add constraint FK3fyj7xb34y2nysfhty0nhnqcj 
       foreign key (rev) 
       references aud;
       
       
    alter table user_account_decision_department drop constraint user_account_decision_department_pkey;
       
    alter table if exists user_account_decision_department
       rename column department to department_code;
       
    alter table if exists user_account_decision_department
       add column department uuid;
       
    update user_account_decision_department set department = (select d.id from decision_department d where d.code = department_code);
    
    delete from user_account_decision_department where department is null;
    
    alter table if exists user_account_decision_department
       alter column department set not null;
       
    alter table if exists user_account_decision_department
       drop column department_code;
       
    alter table user_account_decision_department add primary key (user_account_id, department);

    alter table if exists user_account_decision_department 
       add constraint FK8ybrl26sm8lvjjv40obm1jpap 
       foreign key (department) 
       references decision_department;
