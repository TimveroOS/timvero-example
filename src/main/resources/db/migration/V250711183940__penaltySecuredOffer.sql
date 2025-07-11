
create table aud_penalty_secured_offer (
                                           id bigint not null,
                                           rev integer not null,
                                           primary key (rev, id)
);

create table penalty_secured_offer (
                                       id bigint not null,
                                       primary key (id)
);

alter table if exists aud_penalty_secured_offer
    add constraint FK1g8b10jcpr5h21ocr37xuf44e
        foreign key (rev, id)
            references aud_secured_offer;

alter table if exists penalty_secured_offer
    add constraint FK9f3lugpsrklmtyohvsljdca08
        foreign key (id)
            references secured_offer;
