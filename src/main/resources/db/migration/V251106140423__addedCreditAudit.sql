
create table aud_credit (
                            id uuid not null,
                            rev integer not null,
                            credit_type integer not null,
                            revtype smallint,
                            calculation_date date,
                            created_at timestamp(6) with time zone,
                            start_date date,
                            updated_at timestamp(6) with time zone,
                            actual_snapshot bigint,
                            created_by uuid,
                            updated_by uuid,
                            application_id uuid,
                            condition uuid,
                            primary key (rev, id)
);

create table aud_credit_credit_operation (
                                             rev integer not null,
                                             credit_id uuid not null,
                                             id uuid not null,
                                             revtype smallint,
                                             primary key (credit_id, rev, id)
);

create table aud_credit_credit_snapshot (
                                            rev integer not null,
                                            credit_id uuid not null,
                                            id bigint not null,
                                            revtype smallint,
                                            primary key (credit_id, rev, id)
);

create table aud_credit_snapshot (
                                     id bigint not null,
                                     rev integer not null,
                                     revtype smallint,
                                     date date,
                                     debt jsonb,
                                     credit_status integer,
                                     primary key (rev, id)
);

alter table if exists aud_credit
    add constraint FKbdlbf4yucl1qufsunbl25e97
        foreign key (rev)
            references aud;

alter table if exists aud_credit_credit_operation
    add constraint FK5ytgtcpn3vndk303vahmc73cc
        foreign key (rev)
            references aud;

alter table if exists aud_credit_credit_snapshot
    add constraint FK3wwyjacq6qi9c2fpy4dd563ax
        foreign key (rev)
            references aud;

alter table if exists aud_credit_snapshot
    add constraint FKhpq1s5qeqdt0arfnd4bbl3ajg
        foreign key (rev)
            references aud;
