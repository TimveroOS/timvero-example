package com.timvero.example.admin.client.entity;

import com.timvero.base.entity.AbstractAuditable;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.Participant_;
import com.timvero.ground.entity.NamedEntity;
import com.timvero.structure.notification.entity.Address;
import com.timvero.structure.notification.entity.Notifiable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Table(name = "client")
@Audited
@Indexed
public class Client extends AbstractAuditable implements NamedEntity, Notifiable {

    @Embedded
    @IndexedEmbedded
    private IndividualInfo individualInfo;

    @Embedded
    @IndexedEmbedded
    private ContactInfo contactInfo;

    @OneToMany(mappedBy = Participant_.CLIENT)
    private Set<Participant> participants;

    public IndividualInfo getIndividualInfo() {
        return individualInfo;
    }

    public void setIndividualInfo(IndividualInfo individualInfo) {
        this.individualInfo = individualInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    @Transient
    public Set<Application> getApplications() {
        return getParticipants().stream()
            .map(Participant::getApplication)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transient
    public Set<ExampleCredit> getCredits() {
        return getApplications().stream()
            .map(Application::getCredit)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public String getDisplayedName() {
        return getIndividualInfo().getFullName();
    }

    @Override
    public String getNotificationEmail() {
        return getContactInfo().getEmail();
    }

    @Override
    public String getMainNotificationPhone() {
        return getContactInfo().getPhone();
    }

    @Override
    public Address getNotificationAddress() {
        return null;
    }
}
