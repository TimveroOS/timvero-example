package com.timvero.example.admin.participant.document;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.document.ApplicationContractDocumentCategory.ContractDocumentModel;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.scheduled.entity.PaymentSchedule;
import com.timvero.scheduled.entity.PaymentSegment;
import com.timvero.structure.template.entity.DocumentType;
import com.timvero.structure.template.generation.DocumentCategory;
import com.timvero.structure.template.generation.DocumentModel;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContractDocumentCategory
    extends DocumentCategory<UUID, Participant, ContractDocumentModel> {

    public static final DocumentType TYPE = new DocumentType("APPLICATION_CONTRACT");

    @Override
    public DocumentType getType() {
        return TYPE;
    }

    @Override
    protected ContractDocumentModel getModel(Participant participant) {
        //ExampleCreditCondition condition = participant.getApplication().getCondition();
        PaymentSchedule paymentSchedule = null; // TODO
        //    creditScheduledService.getPaymentSchedule(condition, condition.getPrincipal(), LocalDate.now());
        return new ContractDocumentModel(participant.getApplication(), paymentSchedule);
    }

    @Override
    protected boolean isSuitableTestEntity(Participant participant) {
        return participant.getStatus() == ParticipantStatus.APPROVED
            /* TODO && participant.getApplication().getCondition() != null*/;
    }

    public static class ContractDocumentModel extends DocumentModel {

        private Application application;
        private PaymentSchedule paymentSchedule;

        public ContractDocumentModel() {
        }

        public ContractDocumentModel(Application application, PaymentSchedule paymentSchedule) {
            this.application = application;
            this.paymentSchedule = paymentSchedule;
        }

        public Application getApplication() {
            return application;
        }

        public PaymentSchedule getPaymentSchedule() {
            return paymentSchedule;
        }

        public PaymentSegment getFirstPayment() {
            return paymentSchedule.getPayments().firstEntry().getValue();
        }
    }
}
