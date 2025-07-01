package com.timvero.example.admin.participant.checker;

import static com.timvero.example.admin.CustomConfiguration.PARTICIPANT_TREE;

import com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.flowable.internal.service.DecisionProcessStarter;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import com.timvero.ground.document.EntityDocument;
import com.timvero.ground.document.EntityDocumentFinder;
import com.timvero.ground.document.EntityDocumentService;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignableDocument_;
import com.timvero.ground.document.signable.SignatureStatus;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowerStartTreeChecker extends EntityChecker<Participant, UUID> {

    @Autowired
    private DecisionProcessStarter decisionProcessStarter;
    @Autowired
    private EntityDocumentService documentService;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EntityDocumentFinder documentFinder;

    @Override
    protected void registerListeners(CheckerListenerRegistry<Participant> registry) {
        registry.entityChange(SignableDocument.class, d -> participantRepository.getReferenceById(d.getOwnerId()))
            .updated(SignableDocument_.STATUS)
            .and(d -> d.getStatus() == SignatureStatus.SIGNED && d.getDocumentType() == ParticipantDocumentTypesConfiguration.APPLICATION_FORM);

        registry.entityChange(EntityDocument.class, d -> participantRepository.getReferenceById(d.getOwnerId()))
            .inserted().and(d -> {
                Participant participant = participantRepository.getReferenceById(d.getOwnerId());
                return participant != null &&
                    documentService.getRequiredDocumentTypes(participant).contains(d.getDocumentType());
            });
    }

    @Override
    protected boolean isAvailable(Participant participant) {
        return participant.getRoles().contains(ParticipantRole.BORROWER)
            && participant.getStatus() == ParticipantStatus.NEW
            && documentFinder.isLatestSigned(participant, ParticipantDocumentTypesConfiguration.APPLICATION_FORM)
            && documentService.requiredDocumentsAdded(participant);
    }

    @Override
    protected void perform(Participant participant) {

        participant.setStatus(ParticipantStatus.IN_PROCESS);
        decisionProcessStarter.start(PARTICIPANT_TREE, participant.getId());
    }
}
