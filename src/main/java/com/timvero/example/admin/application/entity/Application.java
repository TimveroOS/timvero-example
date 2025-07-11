package com.timvero.example.admin.application.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;

import com.timvero.base.entity.AbstractAuditable;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import com.timvero.example.admin.participant.entity.Participant_;
import com.timvero.ground.entity.NamedEntity;
import com.timvero.ground.util.Lang;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = Participant_.APPLICATION, cascade = ALL, fetch = EAGER)
    private Set<Participant> participants;

/*    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "condition")
    @Fetch(FetchMode.JOIN)
    private ExampleCreditCondition condition;*/

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }


    public Participant getBorrowerParticipant() {
        return getParticipants().stream().filter(p -> p.getRoles().contains(ParticipantRole.BORROWER))
            .collect(Lang.exactlyOne());
    }

    public void setBorrowerParticipant(Participant participant) {
        participant.getRoles().add(ParticipantRole.BORROWER);
        participant.setApplication(this);
        getParticipants().add(participant);
    }

    public Set<Participant> getParticipants() {
        return participants != null ? participants : (participants = new HashSet<>());
    }

    @Override
    public String getDisplayedName() {
        return getBorrowerParticipant().getDisplayedName();
    }

  /*  public ExampleCreditCondition getCondition() {
        return condition;
    }

    public void setCondition(ExampleCreditCondition condition) {
        this.condition = condition;
    }*/
}
