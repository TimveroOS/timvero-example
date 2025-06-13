package com.timvero.example.admin.participant.entity;


import com.timvero.ground.entity.enums.InEnum;

public enum ParticipantRole implements InEnum<ParticipantRole> {
    BORROWER,
    GUARANTOR,
    COLLATERAL;
}
