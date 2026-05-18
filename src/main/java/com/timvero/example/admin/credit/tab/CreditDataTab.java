package com.timvero.example.admin.credit.tab;

import static com.timvero.example.admin.credit.filter.LoanEventType.FEE;
import static com.timvero.example.admin.credit.filter.LoanEventType.INITIAL;
import static com.timvero.example.admin.credit.filter.LoanEventType.PAYMENT;
import static com.timvero.ground.util.EntityUtils.getClassWithoutInitializingProxy;
import static com.timvero.servicing.credit.entity.operation.OperationStatus.APPROVED;

import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.credit.filter.CreditDataView;
import com.timvero.example.admin.credit.filter.LoanEventType;
import com.timvero.example.admin.operation.charge.ChargeOperation;
import com.timvero.example.admin.operation.pastdue.PastDueOperationService;
import com.timvero.ground.util.Lang;
import com.timvero.scheduled.entity.PaymentSchedule;
import com.timvero.servicing.credit.entity.CreditSnapshot;
import com.timvero.servicing.credit.entity.operation.CreditFee;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.web.common.tab.EntityTabController;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/credit-data")
@Controller
@Order(1000)
public class CreditDataTab extends EntityTabController<UUID, ExampleCredit> {

    private static final Map<Class<? extends CreditOperation>, LoanEventType> OPERATION_EVENTS =
        Map.of(
            ChargeOperation.class, INITIAL,
            CreditFee.class, FEE,
            CreditPayment.class, PAYMENT);

    @Autowired
    private PastDueOperationService pastDueOperationService;

    @Override
    public boolean isVisible(ExampleCredit credit) {
        return true;
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        ExampleCredit credit = loadEntity(id);
        CreditSnapshot snapshot = credit.getActualSnapshot();

        LocalDate currentDate = LocalDate.now();
        model.addAttribute("currentDate", currentDate);

        TreeMap<LocalDate, List<CreditDataView>> dataViews = Lang.concat(
                getSchedulePaymentsDataView(snapshot, credit),
                getActiveOperationsView(credit))
            .collect(Collectors.groupingBy(CreditDataView::getDate, TreeMap::new,
                Collectors.collectingAndThen(Collectors.toList(), l -> {
                    l.sort(Comparator.naturalOrder());
                    return l;
                })));

        model.addAttribute("creditDataViews", dataViews);

        LocalDate paidUntilDate = pastDueOperationService.getPaidUntil(credit);
        if (paidUntilDate == null) {
            paidUntilDate = credit.getStartDate();
        }
        model.addAttribute("paidUntilDate", paidUntilDate);

        List<CreditPayment> payments = credit.getPayments().collect(Collectors.toList());
        payments.sort(Comparator.comparing(CreditPayment::getDate).thenComparing(CreditPayment::getCreatedAt));
        model.addAttribute("payments", payments);

        return super.getTabTemplate(id, model);
    }

    private Stream<CreditDataView> getSchedulePaymentsDataView(CreditSnapshot snapshot, ExampleCredit credit) {
        if (snapshot != null && snapshot.getStatus().in(CreditCalculationConfiguration.VOID)) {
            return Stream.empty();
        }
        PaymentSchedule schedule = credit.getApplication().getPaymentSchedule();
        return schedule != null ? schedule.getPayments().values().stream()
            .map(p -> new CreditDataView(p.getDate(), LoanEventType.SCHEDULE_PAYMENT, p)) : Stream.empty();
    }

    private Stream<CreditDataView> getActiveOperationsView(ExampleCredit credit) {
        return credit.getOperations().stream()
            .filter(o -> APPROVED.equals(o.getStatus()))
            .filter(o -> OPERATION_EVENTS.containsKey(getClassWithoutInitializingProxy(o)))
            .map(o -> new CreditDataView(o.getDate(), OPERATION_EVENTS.get(getClassWithoutInitializingProxy(o)), o));
    }
}
