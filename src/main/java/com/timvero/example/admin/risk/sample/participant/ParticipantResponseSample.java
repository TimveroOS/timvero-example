package com.timvero.example.admin.risk.sample.participant;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.risk.participant.ParticipantFeatureSource;
import com.timvero.example.admin.risk.sample.ExampleResponseSampleType;
import com.timvero.loan.response_sample.entity.ResponseSample;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@DiscriminatorValue(ExampleResponseSampleType.APPLICATION_VALUE)
@Table(name = "participant_response_sample")
public class ParticipantResponseSample extends ResponseSample {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    protected ParticipantResponseSample() {
    }

    public ParticipantResponseSample(Participant participant) {
        super(ParticipantFeatureSource.APPLICATION_DATA_SOURCE_NAME);
        this.participant = participant;
    }

    public Participant getParticipant() {
        return participant;
    }

    @Override
    public String getDisplayedName() {
        return participant.getDisplayedName();
    }
}
