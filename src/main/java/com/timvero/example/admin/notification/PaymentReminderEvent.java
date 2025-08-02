package com.timvero.example.admin.notification;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.pastdue.PastDueOperation;
import com.timvero.example.admin.operation.pastdue.PastDueOperationService;
import com.timvero.ground.event.EntityEventListener;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import com.timvero.servicing.credit.event.CreditCalculationDateChangeEvent;
import com.timvero.structure.notification.entity.EventParameter;
import com.timvero.structure.notification.execution.NotificationEvent;
import com.timvero.structure.notification.execution.NotificationEventType;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentReminderEvent extends NotificationEvent<ExampleCredit, PaymentReminderTemplateModel>
    implements EntityEventListener<CreditCalculationDateChangeEvent> {

    public static final NotificationEventType TYPE = new NotificationEventType("PAYMENT_REMINDER");

    private static final String DAYS_COUNT = "daysCount";
    private static final EventParameter DAYS_COUNT_PARAM = EventParameter.integerField("daysCount");

    @Autowired
    private PastDueOperationService pastDueService;

    public PaymentReminderEvent() {
        super(DAYS_COUNT_PARAM);
    }

    @Override
    public NotificationEventType getType() {
        return TYPE;
    }

    @Override
    public void handle(CreditCalculationDateChangeEvent event) {
        super.notify(event.getCreditId(), new PaymentReminderTemplateModel());
    }

    @Override
    protected boolean isSuitableTestEntity(ExampleCredit credit) {
        return !credit.getActualSnapshot().getStatus().isEnding();
    }

    @Override
    protected boolean entityMatchesTemplate(ExampleCredit credit, PaymentReminderTemplateModel model,
                                          java.util.Map<String, Object> templateParams) {
        if (credit.getActualSnapshot().getStatus().isEnding()) {
            return false;
        }

        // Use the daysCount parameter from template configuration
        if (templateParams.containsKey(DAYS_COUNT)) {
            Integer configuredDaysCount = (Integer) templateParams.get(DAYS_COUNT);
            model.setDaysCount(configuredDaysCount);
            // Only send notification if model's daysCount matches template configuration
            if (configuredDaysCount < 0) {
                model.setReminderType(PaymentReminderType.OVERDUE);
                Optional<MonetaryAmount> totalPastDueAmount =
                    pastDueService.getPastDueTotal(credit.getActualSnapshot());
                if (totalPastDueAmount.isPresent()) {
                    model.setExpectedPaymentDate(credit.getCalculationDate().plusDays(configuredDaysCount));
                    return pastDueService.daysPastDue(credit) == -configuredDaysCount;
                }
            } else {
                model.setReminderType(PaymentReminderType.UPCOMING);
                final LocalDate today = credit.getCalculationDate();
                Optional<LocalDate> nextPaymentDate =
                    credit.getOperations(PastDueOperation.class, OperationStatus.APPROVED).map(CreditOperation::getDate)
                        .filter(d -> !d.isBefore(today)).min(Comparator.naturalOrder());
                if (nextPaymentDate.isPresent()) {
                    model.setExpectedPaymentDate(nextPaymentDate.get());
                    return ChronoUnit.DAYS.between(today, nextPaymentDate.get()) == configuredDaysCount;
                }
            }
        }

        return false;
    }
}
