package com.timvero.example.admin.credit.action;

import static com.timvero.common.validation.ValidationUtils.PATTERN_DATEPICKER_FORMAT;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.ACTIVE;

import com.timvero.example.admin.credit.action.RegisterPaymentAction.ManualTransferForm;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.transaction.BorrowerTransactionService;
import com.timvero.example.admin.transaction.entity.LiquidityClientPaymentMethod;
import com.timvero.ground.action.EntityAction;
import com.timvero.transfer.transaction.entity.TransactionType;
import com.timvero.web.common.action.EntityActionController;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register-payment")
public class RegisterPaymentAction extends EntityActionController<UUID, ExampleCredit, ManualTransferForm> {

    @Autowired
    private BorrowerTransactionService borrowerTransactionService;


    public static final Long OTHER = 0L;

    @Override
    protected EntityAction<? super ExampleCredit, ManualTransferForm> action() {
        return when(c -> c.getActualSnapshot() != null && c.getActualSnapshot().getStatus().equals(ACTIVE))
            .then((c, f, u) -> {
                LiquidityClientPaymentMethod paymentMethod =
                    new LiquidityClientPaymentMethod(f.getProcessedDate(), f.getAmount(), TransactionType.INCOMING,
                        c.getApplication().getBorrowerParticipant().getClient().getIndividualInfo().getFullName());
                borrowerTransactionService.proceedCustom(c, TransactionType.INCOMING, paymentMethod,
                    paymentMethod.getAmount(), true, f.getDescription());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        ExampleCredit credit = loadEntity(id);
        ManualTransferForm form = new ManualTransferForm();
        form.setProcessedDate(LocalDate.now());
        model.addAttribute("form", form);
        model.addAttribute("minDate", credit.getStartDate());
        model.addAttribute("currencies", List.of(credit.getCondition().getPrincipal().getCurrency()));
        model.addAttribute("title", "credit.dialog.register-payment");
        return "/credit/action/register-payment.html";
    }

    @Override
    public String getHighlighted() {
        return BTN_SECONDARY;
    }

    public static class ManualTransferForm {

        @NotNull
        @Positive
        private MonetaryAmount amount;

        @NotNull
        @DateTimeFormat(pattern = PATTERN_DATEPICKER_FORMAT)
        private LocalDate processedDate;

        private String description;

        public MonetaryAmount getAmount() {
            return amount;
        }

        public void setAmount(MonetaryAmount amount) {
            this.amount = amount;
        }

        public LocalDate getProcessedDate() {
            return processedDate;
        }

        public void setProcessedDate(LocalDate processedDate) {
            this.processedDate = processedDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
