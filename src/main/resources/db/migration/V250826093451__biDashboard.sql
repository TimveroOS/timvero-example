
    create table bi_dashboard (
        id uuid not null,
        active boolean not null,
        description varchar(1000),
        embed_config TEXT,
        name varchar(255) not null,
        refresh_interval integer,
        superset_dashboard_id varchar(255),
        superset_dashboard_uuid varchar(36),
        primary key (id)
    );

    alter table if exists user_role 
       add column bi_dashboard_id uuid;

    alter table if exists bi_dashboard 
       drop constraint if exists UKkyk553gnoex902quum3dt4stw;

    alter table if exists bi_dashboard 
       add constraint UKkyk553gnoex902quum3dt4stw unique (superset_dashboard_id);

    alter table if exists bi_dashboard 
       drop constraint if exists UKtjovhtkwnp7m7lv6fcpvqr0e1;

    alter table if exists bi_dashboard 
       add constraint UKtjovhtkwnp7m7lv6fcpvqr0e1 unique (superset_dashboard_uuid);

    alter table if exists user_role 
       add constraint FKr73sxyhx9m5wrjln68egx8pre 
       foreign key (bi_dashboard_id) 
       references bi_dashboard;
