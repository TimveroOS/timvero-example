    alter table if exists user_account 
       add column phone varchar(255);

    alter table if exists user_account 
       drop constraint if exists uk_user_uniqueness_email;

    alter table if exists user_account 
       add constraint uk_user_uniqueness_email unique (email);