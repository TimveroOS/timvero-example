package com.timvero.example.admin.risk.sample.participant;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.risk.sample.ExampleResponseSampleType;
import com.timvero.loan.feature.service.EntityFeatureSource;
import com.timvero.loan.response_sample.entity.ResponseSample;
import com.timvero.loan.response_sample.entity.ResponseSampleType;
import com.timvero.loan.response_sample.service.ResponseSampleManager;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipantResponseSampleManager extends ResponseSampleManager<ParticipantResponseSample> {

    @Autowired
    private Map<String, EntityFeatureSource<Participant>> sources;

    @Override
    public Map<String, Object> getBindings(ParticipantResponseSample sample, String dataTypeName)
        throws IOException {
        return (Map<String, Object>) sources.get(dataTypeName).getBindings(sample.getParticipant());
    }

    @Override
    public String print(ParticipantResponseSample sample) throws IOException {
        return sample.getParticipant().getDisplayedName();
    }

    @Override
    public ResponseSampleType getSampleType() {
        return ExampleResponseSampleType.APPLICATION;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public ResponseSample create(Participant participant) {
        return repositorySupport.getRepository().save(new ParticipantResponseSample(participant));
    }

    public boolean isPresent(Participant participant) {
        return findByParticipant(participant).isPresent();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(Participant participant) {
        ParticipantResponseSample sample = findByParticipant(participant).orElseThrow();
        repositorySupport.getRepository().delete(sample);
    }

    private Optional<ParticipantResponseSample> findByParticipant(Participant participant) {
        return repositorySupport.getSpecificationExecutor().findOne((root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(ParticipantResponseSample_.participant), participant));
    }
}
