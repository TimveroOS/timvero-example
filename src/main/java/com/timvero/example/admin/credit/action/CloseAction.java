package com.timvero.example.admin.credit.action;

import com.timvero.common.validation.ValidationUtils;
import com.timvero.example.admin.credit.action.CloseAction.CloseForm;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.credit.label.CreditPaidLabel;
import com.timvero.example.admin.operation.close.CloseOperationService;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.EntityActionController;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/close")
@Controller
@Order(3000)
public class CloseAction extends EntityActionController<UUID, ExampleCredit, CloseForm> {

    @Autowired
    private CreditPaidLabel creditPaidLabel;

    @Autowired
    private CloseOperationService closeOperationService;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super ExampleCredit, CloseForm> action() {
        return when(c -> creditPaidLabel.isEntityMarked(c))
            .then((c, f, u) -> {
                closeOperationService.createAndSaveOperation(c.getId(), f.getCloseDate());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        ExampleCredit credit = loadEntity(id);
        CloseForm form = new CloseForm();
        form.setCloseDate(LocalDate.now());
        model.addAttribute("form", form);
        model.addAttribute("minDate", credit.getActualSnapshot().getDate());
        model.addAttribute("maxDate", credit.getCalculationDate());
        return "/credit/action/close";
    }

    public static class CloseForm {

        @NotNull
        @DateTimeFormat(pattern = ValidationUtils.PATTERN_DATEPICKER_FORMAT)
        private LocalDate closeDate;

        public LocalDate getCloseDate() {
            return closeDate;
        }

        public void setCloseDate(LocalDate closeDate) {
            this.closeDate = closeDate;
        }
    }
}
