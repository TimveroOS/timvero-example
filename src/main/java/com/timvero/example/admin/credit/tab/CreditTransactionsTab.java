package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.transaction.entity.BorrowerTransaction;
import com.timvero.example.admin.transaction.entity.BorrowerTransactionRepository;
import com.timvero.web.common.tab.EntityTabController;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/transactions")
@Controller
@Order(8000)
public class CreditTransactionsTab extends EntityTabController<UUID, ExampleCredit> {

    @Autowired
    private BorrowerTransactionRepository borrowerTransactionRepository;

    @Override
    public boolean isVisible(ExampleCredit credit) {
        return borrowerTransactionRepository.existsByCreditId(credit.getId());
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        ExampleCredit credit = loadEntity(id);

        Collection<BorrowerTransaction> transactions = borrowerTransactionRepository.findAllByCredit(credit)
            .stream().sorted(Comparator.comparing(BorrowerTransaction::getCreatedAt).reversed()).toList();

        model.addAttribute("transactions", transactions);
        return super.getTabTemplate(id, model);
    }
}
