package com.timvero.example.admin.participant.checker;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import com.timvero.ground.document.EntityDocumentFinder;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignableDocument_;
import com.timvero.ground.document.signable.SignatureStatus;
import com.timvero.servicing.engine.CreditCalculationService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractSignChecker extends EntityChecker<Application, UUID> {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityDocumentFinder documentFinder;
    @Autowired
    private CreditCalculationService calculationService;

    @Override
    protected void registerListeners(CheckerListenerRegistry<Application> registry) {
        registry
            .entityChange(SignableDocument.class,
                d -> participantRepository.getReferenceById(d.getOwnerId()).getApplication())
            .updated(SignableDocument_.STATUS).and(d -> d.getStatus() == SignatureStatus.SIGNED
                && d.getDocumentType() == ParticipantDocumentTypesConfiguration.APPLICATION_CONTRACT);
    }

    @Override
    protected boolean isAvailable(Application application) {
        return application.getStatus().equals(ApplicationStatus.PENDING_CONTRACT_SIGNATURE);
    }

    @Override
    protected void perform(Application application) {
        application.setStatus(ApplicationStatus.SERVICING);

        LocalDate signDate = documentFinder
            .latest(application.getBorrowerParticipant(), ParticipantDocumentTypesConfiguration.APPLICATION_CONTRACT)
            .get().getDecisionMadeAt().atZone(ZoneId.systemDefault()).toLocalDate();

        ExampleCredit credit = new ExampleCredit();
        credit.setApplication(application);
        credit.setCondition(application.getCondition());
        credit.setStartDate(signDate);
        entityManager.persist(credit);

        calculationService.calculate(credit.getId(), signDate, signDate);
    }
}
