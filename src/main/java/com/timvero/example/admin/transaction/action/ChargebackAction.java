package com.timvero.example.admin.transaction.action;

import com.timvero.example.admin.transaction.BorrowerTransactionService;
import com.timvero.example.admin.transaction.entity.BorrowerTransaction;
import com.timvero.ground.action.EntityAction;
import com.timvero.transfer.transaction.entity.TransactionStatus;
import com.timvero.transfer.transaction.entity.TransactionType;
import com.timvero.web.common.action.EntityActionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;

//@RequestMapping("/chargeback")
//@Controller
@Order(1_000)
public class ChargebackAction extends EntityActionController<Long, BorrowerTransaction, Object> {

    @Autowired
    private BorrowerTransactionService borrowerTransactionService;

    @Override
    public String getHighlighted() {
        return BTN_SECONDARY;
    }

    @Override
    protected EntityAction<? super BorrowerTransaction, Object> action() {
        return when(t -> !t.getStatus().in(TransactionStatus.CHARGEBACK, TransactionStatus.FAILED)
            && t.getType() == TransactionType.INCOMING)
            .then((t, f, u) -> {
                borrowerTransactionService.registerChargeback(t);
            });
    }

    @Override
    protected String getActionTemplate(Long aLong, Model model, String actionPath) throws Exception {
        model.addAttribute("title", "credit.dialog.action.chargeback.title");
        model.addAttribute("message", "credit.dialog.action.chargeback.message");
        return "/common/action/yes-no";
    }

}
