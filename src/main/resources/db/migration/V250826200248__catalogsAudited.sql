
    create table aud_bi_dashboard (
        id uuid not null,
        rev integer not null,
        revtype smallint,
        active boolean,
        description varchar(1000),
        embed_config TEXT,
        name varchar(255),
        refresh_interval integer,
        superset_dashboard_id varchar(255),
        superset_dashboard_uuid varchar(36),
        primary key (rev, id)
    );

    alter table if exists aud_decision_department 
       add column active boolean;

    alter table if exists aud_product_category 
       add column active boolean;

    alter table if exists aud_reference_amount 
       add column active boolean;

    alter table if exists aud_reference_rate 
       add column active boolean;

    alter table if exists aud_bi_dashboard 
       add constraint FK84inufhbyvm1fdr24lxbemj0y 
       foreign key (rev) 
       references aud;
