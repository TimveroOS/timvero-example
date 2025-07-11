package com.timvero.example.admin.offer.action;

import com.timvero.application.procuring.action.SelectConditionAction;
import com.timvero.application.procuring.entity.SecuredOffer;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationRepository;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.example.admin.offer.form.ConditionForm;
import com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.signable.SignableDocumentService;
import com.timvero.loan.engine.CreditScheduledService;
import com.timvero.scheduled.entity.PaymentSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/submit-regular")
public class SelectRegularConditionAction extends SelectConditionAction<ExampleProductOffer, ConditionForm> {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CreditScheduledService scheduledService;

    @Autowired
    private SignableDocumentService documentService;

    @Override
    protected String getActionTemplate(Long id, Model model, String actionPath, ExampleProductOffer productOffer,
        SecuredOffer securedOffer) throws Exception {
        model.addAttribute("conditionForm", new ConditionForm());
        return "/application/action/submit-offer";
    }

    @Override
    protected EntityAction<? super ExampleProductOffer, ConditionForm> action() {
        return when(o -> o.getApplication().getCondition() == null
            && o.getApplication().getStatus().equals(ApplicationStatus.CONDITION_CHOOSING)
            && o.getParticipant().getStatus() == ParticipantStatus.APPROVED).then((offer, form, user) -> {
                Application application = offer.getApplication();
                ExampleSecuredOffer securedOffer =
                    (ExampleSecuredOffer) findSecuredOffer(offer, form.getSecuredOfferKey());

                ExampleCreditCondition condition =
                    new ExampleCreditCondition(form.getPrincipal(), offer.getCreditProduct().getEngineName(),
                        offer.getProductAdditive().getInterestRate(), form.getTerm(), securedOffer);
                application.setCondition(condition);

                PaymentSchedule paymentSchedule =
                    scheduledService.getPaymentSchedule(condition, form.getPrincipal(), form.getStart());
                application.setPaymentSchedule(paymentSchedule);

                application.setStatus(ApplicationStatus.PENDING_CONTRACT_SIGNATURE);

                applicationRepository.save(application);

                documentService.generate(application.getBorrowerParticipant(),
                    ParticipantDocumentTypesConfiguration.APPLICATION_CONTRACT,
                    offer.getCreditProduct().getUuidContractTemplate());
            });
    }
}
