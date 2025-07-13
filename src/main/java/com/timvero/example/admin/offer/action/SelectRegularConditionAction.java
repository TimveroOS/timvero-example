package com.timvero.example.admin.offer.action;

import static com.timvero.servicing.engine.CreditCalculatorUtils.periodicInterest;

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
import com.timvero.ground.util.MonetaryUtil;
import com.timvero.loan.engine.CreditScheduledService;
import com.timvero.loan.engine.util.PaymentCalculator;
import com.timvero.scheduled.day_count.Method_30_360_BB;
import com.timvero.scheduled.entity.PaymentSchedule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import javax.money.MonetaryAmount;
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
        ConditionForm conditionForm = new ConditionForm();
        conditionForm.setPrincipal(MonetaryUtil.of(productOffer.getMaxAmount(), productOffer.getCurrency()));
        conditionForm.setTerm(productOffer.getMaxTerm());
        conditionForm.setStart(LocalDate.now());
        model.addAttribute("conditionForm", conditionForm);
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

                Period period = Period.ofMonths(1);
                MonetaryAmount principal = form.getPrincipal();
                BigDecimal interestRate = offer.getProductAdditive().getInterestRate();
                Integer term = form.getTerm();

                MonetaryAmount regularPayment = PaymentCalculator.calcAnnuityPayment(principal,
                    MonetaryUtil.zero(principal.getCurrency()), periodicInterest(period, interestRate), term, 0);

                ExampleCreditCondition condition = new ExampleCreditCondition(principal,
                    offer.getCreditProduct().getEngineName(), interestRate, offer.getCreditProduct().getLateFeeRate(),
                    Method_30_360_BB.NAME, period, term, regularPayment, securedOffer);
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
