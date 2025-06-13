package com.timvero.example.admin.participant.entity;

import com.timvero.base.entity.SynchronousAccessRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository
    extends JpaRepository<Participant, UUID>, SynchronousAccessRepository<Participant, UUID> {

}
