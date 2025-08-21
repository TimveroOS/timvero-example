package com.timvero.example.admin.participant;

import com.timvero.example.admin.participant.document.ApplicationContractDocumentCategory;
import com.timvero.example.admin.participant.document.ApplicationFormDocumentCategory;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.ground.document.DocumentTypeAssociation;
import com.timvero.ground.document.enums.EntityDocumentType;
import com.timvero.ground.document.signable.SignableDocumentType;
import java.util.function.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParticipantDocumentTypesConfiguration {

    public static final SignableDocumentType APPLICATION_FORM =
        new SignableDocumentType("APPLICATION_FORM", ApplicationFormDocumentCategory.TYPE);
    public static final SignableDocumentType APPLICATION_CONTRACT =
        new SignableDocumentType("APPLICATION_CONTRACT", ApplicationContractDocumentCategory.TYPE);

    public static final EntityDocumentType OTHER = EntityDocumentType.OTHER;
    public static final EntityDocumentType ID_SCAN = new EntityDocumentType("ID_SCAN");

    private static final Predicate<Participant> PARTICIPANT_GUARANTOR =
        participant -> participant.getRoles().contains(ParticipantRole.GUARANTOR);
    private static final Predicate<Participant> PARTICIPANT_BORROWER =
        participant -> participant.getRoles().contains(ParticipantRole.BORROWER);

    @Bean
    DocumentTypeAssociation<Participant> idScanDocumentTypeAssociations() {
        return DocumentTypeAssociation.forEntityClass(Participant.class).required(ID_SCAN)
            .predicate(participant -> participant.getStatus().in(ParticipantStatus.NEW))
            .predicate(PARTICIPANT_GUARANTOR.or(PARTICIPANT_BORROWER)).build();
    }

    @Bean
    DocumentTypeAssociation<Participant> otherDocumentTypeAssociations() {
        return DocumentTypeAssociation.forEntityClass(Participant.class).uploadable(OTHER).build();
    }
}
