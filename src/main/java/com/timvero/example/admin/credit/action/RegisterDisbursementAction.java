package com.timvero.example.admin.credit.action;

import static com.timvero.common.validation.ValidationUtils.PATTERN_DATEPICKER_FORMAT;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PENDING;

import com.timvero.example.admin.credit.action.RegisterDisbursementAction.ManualDisbursementForm;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.transaction.BorrowerTransactionService;
import com.timvero.example.admin.transaction.entity.LiquidityClientPaymentMethod;
import com.timvero.ground.action.EntityAction;
import com.timvero.transfer.transaction.entity.TransactionType;
import com.timvero.web.common.action.EntityActionController;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register-disbursement")
public class RegisterDisbursementAction extends EntityActionController<UUID, ExampleCredit, ManualDisbursementForm> {

    @Autowired
    private BorrowerTransactionService borrowerTransactionService;


    public static final Long OTHER = 0L;

    @Override
    protected EntityAction<? super ExampleCredit, ManualDisbursementForm> action() {
        return when(c -> c.getActualSnapshot() != null && c.getActualSnapshot().getStatus().equals(PENDING))
            .then((c, f, u) -> {
                LiquidityClientPaymentMethod paymentMethod =
                    new LiquidityClientPaymentMethod(f.getProcessedDate(), c.getCondition().getPrincipal(), TransactionType.OUTGOING,
                        c.getApplication().getBorrowerParticipant().getClient().getIndividualInfo().getFullName());
                borrowerTransactionService.proceedCustom(c, TransactionType.OUTGOING, paymentMethod,
                    paymentMethod.getAmount(), true, f.getDescription());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        ExampleCredit credit = loadEntity(id);
        ManualDisbursementForm form = new ManualDisbursementForm();
        form.setProcessedDate(credit.getStartDate());
        model.addAttribute("form", form);
        model.addAttribute("minDate", credit.getStartDate());
        model.addAttribute("title", "credit.dialog.register-disbursement");
        return "/credit/action/register-disbursement.html";
    }

    @Override
    public String getHighlighted() {
        return BTN_SECONDARY;
    }

    public static class ManualDisbursementForm {

        @NotNull
        @DateTimeFormat(pattern = PATTERN_DATEPICKER_FORMAT)
        private LocalDate processedDate;

        private String description;

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
