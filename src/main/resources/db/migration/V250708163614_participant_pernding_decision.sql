alter table if exists participant
    add column if not exists pending_decision_holder_id bigint;

alter table if exists pending_decision_holder
    add column if not exists participant_id uuid;

insert into pending_decision_holder (owner_type, participant_id)
    select 'PARTICIPANT', p.id
    from participant p
    where not exists (
        select 1 from pending_decision_holder pdh
        where pdh.participant_id = p.id
    );

    update participant
    set pending_decision_holder_id = (
        select pdh.id
        from pending_decision_holder pdh
        where pdh.participant_id = participant.id
    )
    where pending_decision_holder_id is null;

    alter table if exists participant
        alter column pending_decision_holder_id set not null;

    alter table if exists participant
        drop constraint if exists ukg15a8jht6t238dm9vawluxkyg;

    alter table if exists participant
        add constraint ukg15a8jht6t238dm9vawluxkyg unique (pending_decision_holder_id);

    alter table if exists participant
        drop constraint if exists fkhl3vrhpkn1klkfay6wrs0e4l8;

    alter table if exists participant
        add constraint fkhl3vrhpkn1klkfay6wrs0e4l8
            foreign key (pending_decision_holder_id)
                references pending_decision_holder(id);

    alter table if exists pending_decision_holder
        drop constraint if exists fkpending_decision_holder_participant;

    alter table if exists pending_decision_holder
        add constraint fkpending_decision_holder_participant
            foreign key (participant_id)
                references participant(id);
