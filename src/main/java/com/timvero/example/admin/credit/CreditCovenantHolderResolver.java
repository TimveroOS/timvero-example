package com.timvero.example.admin.credit;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.credit.entity.ExampleCreditRepository;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import com.timvero.loan.covenantexecution.CovenantHolderResolver;
import com.timvero.loan.covenantspecification.entity.CovenantSpecification;
import com.timvero.loan.covenantspecification.entity.CovenantSpecificationRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditCovenantHolderResolver implements CovenantHolderResolver<ExampleCredit> {

    @Autowired
    private ExampleCreditRepository creditRepository;
    @Autowired
    private CovenantSpecificationRepository covenantSpecificationRepository;

    @Override
    public Stream<ExampleCredit> resolveTargets(CovenantSpecification specification) {
        return specification.getAdditives().stream()
            .flatMap(a -> creditRepository.getAllByAdditiveId(a));
    }

    @Override
    public List<SubjectRecord<ExampleCredit>> getCovenantSubjects() {
        return List.of(
            // BORROWER subject
            SubjectRecordBuilder.<ExampleCredit>builder()
                .name(ParticipantRole.BORROWER.name())
                .subject(new SubjectSupplierRecord<>(
                    Participant.class,
                    credit -> List.of(credit.getApplication().getBorrowerParticipant())
                ))
                .build(),

            // GUARANTOR subject
            SubjectRecordBuilder.<ExampleCredit>builder()
                .name(ParticipantRole.GUARANTOR.name())
                .subject(new SubjectSupplierRecord<>(
                    Participant.class,
                    credit -> credit.getApplication().getParticipants().stream()
                        .filter(p -> p.getRoles().contains(ParticipantRole.GUARANTOR))
                        .collect(Collectors.toList())
                ))
                .build()
        );
    }

    @Override
    public Collection<CovenantSpecification> getCovenantSpecifications(ExampleCredit target) {
        return covenantSpecificationRepository.findAllByAdditiveIdAndActiveTrue(
            target.getCondition().getSecuredOffer().getOriginalOffer().getProductAdditive().getId()
        );
    }
}