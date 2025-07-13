package com.timvero.example.admin.transaction.action;

import com.timvero.example.admin.transaction.BorrowerTransactionService;
import com.timvero.example.admin.transaction.entity.BorrowerTransaction;
import com.timvero.ground.action.EntityAction;
import com.timvero.transfer.transaction.entity.TransactionStatus;
import com.timvero.web.common.action.EntityActionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;

//@RequestMapping("/failed")
//@Controller
@Order(2_000)
public class FailedTransactionAction extends EntityActionController<Long, BorrowerTransaction, Object> {

    @Autowired
    private BorrowerTransactionService borrowerTransactionService;

    @Override
    public String getHighlighted() {
        return BTN_SECONDARY;
    }

    @Override
    protected EntityAction<? super BorrowerTransaction, Object> action() {
        return when(t -> t.getStatus() != TransactionStatus.FAILED)
            .then((t, f, u) -> {
                borrowerTransactionService.failTransaction(t);
            });
    }

    @Override
    protected String getActionTemplate(Long aLong, Model model, String actionPath) throws Exception {
        model.addAttribute("title", "credit.dialog.action.failedTransaction.title");
        model.addAttribute("message", "credit.dialog.action.failedTransaction.message");
        return "/common/action/yes-no";
    }

}
