package com.timvero.example.admin.transaction.action;

import static com.timvero.common.validation.ValidationUtils.PATTERN_DATEPICKER_FORMAT;

import com.timvero.example.admin.transaction.action.SmartAllocationPaymentTransactionAction.SmartAllocationForm;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.entity.reload.ReloadPageHelper;
import com.timvero.servicing.credit.entity.operation.CreditPaymentType;
import com.timvero.transfer.paymenthub.PaymentHubService;
import com.timvero.transfer.paymenthub.entity.PaymentHubTransaction;
import com.timvero.transfer.paymenthub.entity.PaymentHubTransactionRepository;
import com.timvero.web.common.action.EntityActionController;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// tag::action[]
@RequestMapping("/smart-distribute")
@Controller
@Order(1000)
public class SmartAllocationPaymentTransactionAction
    extends EntityActionController<Long, PaymentHubTransaction, SmartAllocationForm> {
    // end::action[]

    @Autowired
    private PaymentHubService paymentHubService;
    @Autowired
    private ReloadPageHelper reloadPageHelper;
    @Autowired
    private PaymentHubTransactionRepository paymentHubTransactionRepository;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super PaymentHubTransaction, SmartAllocationForm> action() {
        return when(t -> t.getStatus().isSuccessful() && t.getAvailableAmount().isPositive())
            .then((t, f, u) -> {
                paymentHubService.allocate(t, f.getDate());
                reloadPageHelper.reload(t);
            });
    }

    @Override
    protected String getActionTemplate(Long id, Model model, String actionPath) throws Exception {
        model.addAttribute("form", new SmartAllocationForm());
        model.addAttribute("transactionId", id);
        return "/transaction/action/smart-distribute";
    }

    @RequestMapping(value = "/get-plan", method = RequestMethod.GET)
    @ResponseBody
    public Map<UUID, Map<CreditPaymentType, MonetaryAmount>> getPlan(@RequestParam("transactionId") Long transactionId,
        @RequestParam("date") String dateString) {
        LocalDate parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(PATTERN_DATEPICKER_FORMAT));
        PaymentHubTransaction transaction = paymentHubTransactionRepository.getReferenceById(transactionId);
        return paymentHubService.findAllocation(transaction, parsedDate);
    }

    public static class SmartAllocationForm {

        @NotNull
        @DateTimeFormat(pattern = PATTERN_DATEPICKER_FORMAT)
        @PastOrPresent
        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

    }
}