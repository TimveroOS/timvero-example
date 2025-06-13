
    create table address (
        id bigserial not null,
        primary key (id)
    );

    create table aud (
        id serial not null,
        action_id bigint,
        auditor_id bigint,
        timestamp timestamp(6) with time zone,
        primary key (id)
    );

    create table aud_calendar_day (
        date date not null,
        rev integer not null,
        revtype smallint,
        day_type varchar(255),
        note text,
        primary key (rev, date)
    );

    create table aud_entity_document (
        id bigint not null,
        rev integer not null,
        revtype smallint,
        document_type varchar(255),
        uuid uuid default gen_random_uuid(),
        document_id bigint,
        primary key (rev, id)
    );

    create table aud_entity_signable_document (
        id bigint not null,
        rev integer not null,
        revtype smallint,
        active boolean,
        decision_made_at timestamp(6) with time zone,
        document_type varchar(255),
        status varchar(255),
        uuid uuid default gen_random_uuid(),
        document_id bigint,
        document_generation_exception_id bigint,
        generated_document_id bigint,
        signature_id bigint,
        template_id bigint,
        primary key (rev, id)
    );

    create table aud_notification (
        id bigint not null,
        rev integer not null,
        revtype smallint,
        notification_generation_exception_id bigint,
        primary key (rev, id)
    );

    create table auditor (
        auditor_type varchar(31) not null,
        id bigserial not null,
        user_account_id bigint,
        primary key (id)
    );

    create table calendar_day (
        date date not null,
        day_type varchar(255),
        note text,
        primary key (date)
    );

    create table document (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        content_encoding varchar(255) default 'identity' not null,
        content_type varchar(255) not null,
        filename varchar(255) not null,
        hash bytea not null,
        meta varchar(4000),
        path varchar(2000) not null,
        storage varchar(255) not null,
        created_by bigint,
        updated_by bigint,
        primary key (id)
    );

    create table document_template (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        active boolean not null,
        lineage_id uuid,
        document_type varchar(255),
        filename varchar(255),
        template_type varchar(255),
        text text,
        title varchar(255),
        created_by bigint,
        updated_by bigint,
        primary key (id)
    );

    create table editable_property (
        id bigserial not null,
        apply_date date,
        property_name varchar(255),
        property_type varchar(255),
        property_value varchar(255),
        primary key (id)
    );

    create table entity_document (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        active boolean not null,
        document_type varchar(255),
        uuid uuid default gen_random_uuid() not null,
        created_by bigint,
        updated_by bigint,
        document_id bigint,
        primary key (id)
    );

    create table entity_document_signature (
        dtype varchar(31) not null,
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        email varchar(255),
        phone varchar(255),
        signer varchar(255),
        created_by bigint,
        updated_by bigint,
        signature_document bigint,
        primary key (id)
    );

    create table entity_signable_document (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        active boolean not null,
        decision_made_at timestamp(6) with time zone,
        document_type varchar(255),
        status varchar(255) not null,
        uuid uuid default gen_random_uuid() not null,
        created_by bigint,
        updated_by bigint,
        document_id bigint,
        document_generation_exception_id bigint,
        generated_document_id bigint,
        signature_id bigint,
        template_id bigint,
        primary key (id)
    );

    create table event_parameter_value (
        template_id bigint not null,
        formatted_value varchar(255),
        name varchar(255) not null,
        primary key (template_id, name)
    );

    create table exception_entity (
        id bigserial not null,
        cause text not null,
        created_at timestamp(6) with time zone not null,
        message text not null,
        service_name varchar(255) not null,
        stack_trace text,
        primary key (id)
    );

    create table notification (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        email varchar(255),
        event_type varchar(255),
        external_id varchar(255),
        type varchar(255),
        notification_recipient varchar(255),
        notification_source varchar(255),
        performed_at timestamp(6) with time zone,
        phone varchar(255),
        recipient varchar(255),
        status varchar(255),
        subject varchar(4000),
        timezone varchar(255),
        created_by bigint,
        updated_by bigint,
        address_id bigint,
        content bigint,
        notification_generation_exception_id bigint,
        notification_template bigint,
        primary key (id)
    );

    create table notification_attachment (
        notification bigint not null,
        document bigint not null
    );

    create table notification_template (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        active boolean not null,
        lineage_id uuid,
        event_type varchar(255) not null,
        type varchar(255) not null,
        late_available boolean not null,
        prevent_duplicate boolean not null,
        recipient_type varchar(255) not null,
        subject varchar(255),
        text text,
        title varchar(255),
        created_by bigint,
        updated_by bigint,
        primary key (id)
    );

    create table shedlock (
        name varchar(64) not null,
        lock_until timestamp(6) with time zone not null,
        locked_at timestamp(6) with time zone not null,
        locked_by varchar(255) not null,
        primary key (name)
    );

    create table user_account (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        active boolean not null,
        email varchar(255),
        login varchar(255) not null,
        password varchar(255) not null,
        password_setup_at timestamp(6) with time zone not null,
        password_single_use boolean not null,
        full_name varchar(255),
        created_by bigint,
        updated_by bigint,
        user_role_id bigint not null,
        primary key (id)
    );

    create table user_account_action (
        id bigserial not null,
        action_type varchar(255),
        created_at timestamp(6) with time zone not null,
        ip varchar(255),
        params varchar(32600),
        servlet_path varchar(255),
        url varchar(32600),
        created_by bigint,
        primary key (id)
    );

    create table user_account_notification_subscription (
        user_account_id bigint not null,
        notification_subscription uuid
    );

    create table user_role (
        id bigserial not null,
        created_at timestamp(6) with time zone not null,
        updated_at timestamp(6) with time zone not null,
        description varchar(32600),
        export_id uuid default gen_random_uuid() not null,
        god boolean not null,
        name varchar(255) not null,
        start_page varchar(255),
        created_by bigint,
        updated_by bigint,
        primary key (id)
    );

    create table user_role_counter (
        user_role bigint not null,
        counter varchar(255)
    );

    create table user_role_permission (
        user_role bigint not null,
        permission varchar(255)
    );

    alter table if exists auditor 
       drop constraint if exists UK_clqcq9lyspxdxcp6o4f3vkelj;

    alter table if exists auditor 
       add constraint UK_clqcq9lyspxdxcp6o4f3vkelj unique (user_account_id);

    alter table if exists editable_property 
       drop constraint if exists UKo3th0o0vl9pcjvxknbu8m23n4;

    alter table if exists editable_property 
       add constraint UKo3th0o0vl9pcjvxknbu8m23n4 unique (property_name, apply_date);

    alter table if exists entity_document 
       drop constraint if exists UK_4q8d71fs0qd0h3b3khk18w7wd;

    alter table if exists entity_document 
       add constraint UK_4q8d71fs0qd0h3b3khk18w7wd unique (document_id);

    alter table if exists entity_document_signature 
       drop constraint if exists UK_4fwubh39aukwrsv01fx8ye0a7;

    alter table if exists entity_document_signature 
       add constraint UK_4fwubh39aukwrsv01fx8ye0a7 unique (signature_document);

    alter table if exists entity_signable_document 
       drop constraint if exists UK_jx9cpmxjlhpi9ttj4cmnndpvu;

    alter table if exists entity_signable_document 
       add constraint UK_jx9cpmxjlhpi9ttj4cmnndpvu unique (document_id);

    alter table if exists entity_signable_document 
       drop constraint if exists UK_sxsmyrjjp6st1veoey0rvkfdd;

    alter table if exists entity_signable_document 
       add constraint UK_sxsmyrjjp6st1veoey0rvkfdd unique (generated_document_id);

    alter table if exists notification 
       drop constraint if exists UK_nuq96l38ro6ansvlkdpder2w6;

    alter table if exists notification 
       add constraint UK_nuq96l38ro6ansvlkdpder2w6 unique (address_id);

    alter table if exists notification 
       drop constraint if exists UK_67qx2enr2g2lwksvq7bxgjgd7;

    alter table if exists notification 
       add constraint UK_67qx2enr2g2lwksvq7bxgjgd7 unique (content);

    alter table if exists user_account 
       drop constraint if exists UK_plpggm55i6uhyv404q6pyu0ub;

    alter table if exists user_account 
       add constraint UK_plpggm55i6uhyv404q6pyu0ub unique (login);

    alter table if exists user_role 
       drop constraint if exists UK_gryx7lwyvl260hgv8ans6ms1q;

    alter table if exists user_role 
       add constraint UK_gryx7lwyvl260hgv8ans6ms1q unique (export_id);

    alter table if exists user_role 
       drop constraint if exists UK_lnth8w122wgy7grrjlu8hjmuu;

    alter table if exists user_role 
       add constraint UK_lnth8w122wgy7grrjlu8hjmuu unique (name);

    alter table if exists aud_calendar_day 
       add constraint FKs806bstrw3pdlchft0sal7ify 
       foreign key (rev) 
       references aud;

    alter table if exists aud_entity_document 
       add constraint FK2ej0c2fyv54ond1ef513m6ayy 
       foreign key (rev) 
       references aud;

    alter table if exists aud_entity_signable_document 
       add constraint FKhvcnakpvvbqslw59x5lxuu3af 
       foreign key (rev) 
       references aud;

    alter table if exists aud_notification 
       add constraint FKik06ax8luvde011yrbcsa9ual 
       foreign key (rev) 
       references aud;

    alter table if exists auditor 
       add constraint FKocv89vmioseh1tk6bssng54hs 
       foreign key (user_account_id) 
       references user_account;

    alter table if exists document 
       add constraint FKgalwyb5jb3qkmqx4un8gn3ibd 
       foreign key (created_by) 
       references auditor;

    alter table if exists document 
       add constraint FKoie4di2pmdom6s4jxamnoev7u 
       foreign key (updated_by) 
       references auditor;

    alter table if exists document_template 
       add constraint FKg53v2gseljuplm0oa3oicrds3 
       foreign key (created_by) 
       references auditor;

    alter table if exists document_template 
       add constraint FKbh19llhc82l24fo5nqcurka0r 
       foreign key (updated_by) 
       references auditor;

    alter table if exists entity_document 
       add constraint FK4989e77nss4ewksgiwvafn0fu 
       foreign key (created_by) 
       references auditor;

    alter table if exists entity_document 
       add constraint FK1qjwi4rqj9ega7yu8whefx4jb 
       foreign key (updated_by) 
       references auditor;

    alter table if exists entity_document 
       add constraint FKq354gkcxrb16scutecpyef9q8 
       foreign key (document_id) 
       references document;

    alter table if exists entity_document_signature 
       add constraint FKgtaa2ylgiap9klv8cwhyrn7py 
       foreign key (created_by) 
       references auditor;

    alter table if exists entity_document_signature 
       add constraint FK6nwf9cnf7vvpu6txi0tpljhm6 
       foreign key (updated_by) 
       references auditor;

    alter table if exists entity_document_signature 
       add constraint FK9kyiksioiafea8sawl6dqmkie 
       foreign key (signature_document) 
       references document;

    alter table if exists entity_signable_document 
       add constraint FK5a6i9gigj8mot95n3eed6fd4d 
       foreign key (created_by) 
       references auditor;

    alter table if exists entity_signable_document 
       add constraint FKo6ojuylk4mrgc9aogwmfwhtq6 
       foreign key (updated_by) 
       references auditor;

    alter table if exists entity_signable_document 
       add constraint FK1pn5t9c6tndauq1jvf44qk2g8 
       foreign key (document_id) 
       references document;

    alter table if exists entity_signable_document 
       add constraint FKe1v07o71i902luaq2p8csgn6y 
       foreign key (document_generation_exception_id) 
       references exception_entity;

    alter table if exists entity_signable_document 
       add constraint FKsxhjquohln95n9w6a6sya1j7s 
       foreign key (generated_document_id) 
       references document;

    alter table if exists entity_signable_document 
       add constraint FK2l70ythrq9owmt6gf8u611wks 
       foreign key (signature_id) 
       references entity_document_signature;

    alter table if exists entity_signable_document 
       add constraint FK93udvvu1ocu1p2x0579uv87sj 
       foreign key (template_id) 
       references document_template;

    alter table if exists event_parameter_value 
       add constraint FKgdsj8iip4ihnrcdk2toa448pj 
       foreign key (template_id) 
       references notification_template;

    alter table if exists notification 
       add constraint FKmcktu79igqh3unl8bqy7atwev 
       foreign key (created_by) 
       references auditor;

    alter table if exists notification 
       add constraint FK8h9wtft5d6c4ec9of0qpwipm7 
       foreign key (updated_by) 
       references auditor;

    alter table if exists notification 
       add constraint FK3u5m0d296myyupyt8q2x2mfwa 
       foreign key (address_id) 
       references address;

    alter table if exists notification 
       add constraint FK870nbgwuc7gxsiajeh5nwhiwd 
       foreign key (content) 
       references document;

    alter table if exists notification 
       add constraint FKq6qjr0ydk964qdjclhttmb10m 
       foreign key (notification_generation_exception_id) 
       references exception_entity;

    alter table if exists notification 
       add constraint FK9h094fs3qd6rfuvfl0edoefno 
       foreign key (notification_template) 
       references notification_template;

    alter table if exists notification_attachment 
       add constraint FKt8a6eegjj317pfn59ay1khyax 
       foreign key (document) 
       references document;

    alter table if exists notification_attachment 
       add constraint FKm9bkx929a5dergkicq2xkeh26 
       foreign key (notification) 
       references notification;

    alter table if exists notification_template 
       add constraint FK777uhjvl58a4l4bg3nrtctusn 
       foreign key (created_by) 
       references auditor;

    alter table if exists notification_template 
       add constraint FKtfc40yp50tkotm8frm0x4lv4l 
       foreign key (updated_by) 
       references auditor;

    alter table if exists user_account 
       add constraint FK6yeu84vklx3ex4omt5s7far5r 
       foreign key (created_by) 
       references auditor;

    alter table if exists user_account 
       add constraint FK7swwxnq0ubgosfv5coteppa9m 
       foreign key (updated_by) 
       references auditor;

    alter table if exists user_account 
       add constraint FKl4w18uc16qrd5bqawvcly3b3b 
       foreign key (user_role_id) 
       references user_role;

    alter table if exists user_account_action 
       add constraint FKrrhwakw3oqym528kadpq0y7ic 
       foreign key (created_by) 
       references auditor;

    alter table if exists user_account_notification_subscription 
       add constraint FKr4kwaq0l1rkvhojhhwwxpvktj 
       foreign key (user_account_id) 
       references user_account;

    alter table if exists user_role 
       add constraint FKsr2fb6u9y8fifnjekyjvfb5ln 
       foreign key (created_by) 
       references auditor;

    alter table if exists user_role 
       add constraint FKgq1c1ol3nua4mj8q7pv5rnx1e 
       foreign key (updated_by) 
       references auditor;

    alter table if exists user_role_counter 
       add constraint FK9tnsgtpoh11wanveshbfqdmdn 
       foreign key (user_role) 
       references user_role;

    alter table if exists user_role_permission 
       add constraint FKdp4n5wqsigb1asmt50bpgaxy4 
       foreign key (user_role) 
       references user_role;
