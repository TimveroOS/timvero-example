package com.timvero.example.admin.credit.action;

import static com.timvero.common.validation.ValidationUtils.PATTERN_DATEPICKER_FORMAT;

import com.timvero.example.admin.credit.CreditPaymentService;
import com.timvero.example.admin.credit.action.ChangePaymentDateAction.ChangePaymentDateForm;
import com.timvero.ground.action.EntityAction;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
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
@RequestMapping("/change-payment-date")
public class ChangePaymentDateAction extends EntityActionController<UUID, CreditPayment, ChangePaymentDateForm> {

    @Autowired
    private CreditPaymentService creditPaymentService;

    @Override
    protected EntityAction<? super CreditPayment, ChangePaymentDateForm> action() {
        return when(p -> p.getStatus() == OperationStatus.APPROVED)
            .then((p, f, u) -> {
                creditPaymentService.movePaymentDate(p, f.getDate());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        CreditPayment creditPayment = loadEntity(id);
        ChangePaymentDateForm form = new ChangePaymentDateForm();
        form.setDate(creditPayment.getDate());
        model.addAttribute("form", form);
        model.addAttribute("minDate", creditPaymentService.getCreditForPayment(creditPayment).getStartDate());
        return "/credit/action/change-payment-date.html";
    }

    public static class ChangePaymentDateForm {

        @NotNull
        @DateTimeFormat(pattern = PATTERN_DATEPICKER_FORMAT)
        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}
