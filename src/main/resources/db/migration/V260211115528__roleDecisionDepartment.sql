
    create table user_role_decision_department (
        user_role_id uuid not null,
        department uuid not null,
        primary key (user_role_id)
    );

    drop table if exists user_account_decision_department;

    alter table if exists user_role_decision_department
       add constraint FKh6g41dcuim0gd88teegom9mpj
       foreign key (department)
       references decision_department;

    alter table if exists user_role_decision_department
       add constraint FKk34lv5q0fwb3152t0nmupmq2u
       foreign key (user_role_id)
       references user_role;
