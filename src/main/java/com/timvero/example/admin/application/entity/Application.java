package com.timvero.example.admin.application.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;

import com.timvero.base.entity.AbstractAuditable;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.Participant_;
import com.timvero.ground.entity.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Table
@Audited
@Indexed
public class Application extends AbstractAuditable<UUID> implements NamedEntity {

    @Column(nullable = false)
    @Enumerated(STRING)
    private ApplicationStatus status = ApplicationStatus.NEW;

    @OneToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(unique = true, nullable = false, updatable = false)
    private Participant borrowerParticipant;

    @OneToMany(mappedBy = Participant_.APPLICATION, cascade = ALL)
    private Set<Participant> participants;

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Participant getBorrowerParticipant() {
        return borrowerParticipant;
    }

    public void setBorrowerParticipant(Participant borrowerParticipant) {
        this.borrowerParticipant = borrowerParticipant;
    }

    public Set<Participant> getParticipants() {
        return participants != null ? participants : (participants = new HashSet<>());
    }

    @Override
    public String getDisplayedName() {
        return getBorrowerParticipant().getDisplayedName();
    }
}
