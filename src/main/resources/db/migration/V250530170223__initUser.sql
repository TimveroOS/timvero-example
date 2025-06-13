insert into user_role (id, created_at, updated_at, description, god, name, start_page)
values ('36bdc7fc-9e24-424a-a6bc-cd5c70a9c4dd', NOW(), NOW(), '', true, 'Global Admin', '/user');

insert into user_account (id, created_at, updated_at, active, full_name, login, password,
                          user_role_id, password_setup_at, password_single_use)
values ('bd2caa52-a55c-45ed-b227-be5336b9e118', NOW(), NOW(), true, 'Admin', 'admin', '$2a$10$u2DF.MohbA7vs9Zc1WGMS.PWufsSwHjGDp5IPQ9/I.U4QgREjPrAG',
        '36bdc7fc-9e24-424a-a6bc-cd5c70a9c4dd', NOW(), false);

insert into auditor (id, auditor_type, user_account_id)
values('8ce91318-e581-414c-b070-b5f8f1f8688b', 'USER', 'bd2caa52-a55c-45ed-b227-be5336b9e118');
