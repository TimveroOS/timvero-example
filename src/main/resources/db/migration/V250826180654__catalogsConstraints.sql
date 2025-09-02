
    alter table if exists decision_fact_fixed 
       add constraint FKig9v62lmbd14c6jdhvii2479b 
       foreign key (decision_department) 
       references decision_department;

    alter table if exists decision_fact_fixed 
       add constraint FKhlbu9r4xcuuhpuqc22b91sduk 
       foreign key (decline_reason_id) 
       references decline_reason;

    alter table if exists pending_decision 
       add constraint FKx71mbc2nk89wtwltvnb10m0c 
       foreign key (decision_department) 
       references decision_department;

    alter table if exists pending_decision 
       add constraint FK9ebi8la1sk3s7kfl6pfcgq2ro 
       foreign key (decline_reason_id) 
       references decline_reason;

    alter table decision_department
       drop column created_at,
       drop column updated_at,
       drop column created_by,
       drop column updated_by;

    alter table reference_amount
       drop column created_at,
       drop column updated_at,
       drop column created_by,
       drop column updated_by;

    alter table reference_rate
       drop column created_at,
       drop column updated_at,
       drop column created_by,
       drop column updated_by;