package com.timvero.example.admin.client.action;

import static com.timvero.common.validation.ValidationUtils.PATTERN_DATEPICKER_FORMAT;

import com.timvero.example.admin.client.action.AddTransactionClientAction.ClientTransactionForm;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.example.admin.paymentmethod.entity.hub.HubPaymentMethod;
import com.timvero.ground.action.EntityAction;
import com.timvero.transfer.paymenthub.PaymentHubService;
import com.timvero.transfer.paymenthub.entity.PaymentHubTransaction;
import com.timvero.transfer.paymenthub.entity.PaymentHubTransactionRepository;
import com.timvero.transfer.transaction.entity.TransactionStatus;
import com.timvero.transfer.transaction.entity.TransactionType;
import com.timvero.web.common.action.EntityActionController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// tag::action[]
@RequestMapping("/add-transaction")
@Controller
public class AddTransactionClientAction extends EntityActionController<UUID, Client, ClientTransactionForm> {
    // end::action[]

    @Autowired
    private PaymentHubTransactionRepository transactionRepository;
    @Autowired
    private PaymentHubService paymentHubService;

    @Override
    protected EntityAction<? super Client, ClientTransactionForm> action() {
        return when(client -> true)
            .then((c, f, u) -> {
                PaymentHubTransaction transaction = new PaymentHubTransaction();

                transaction.setType(TransactionType.INCOMING);
                transaction.setStatus(TransactionStatus.SUCCEED);
                transaction.setPurpose(CreditCalculationConfiguration.GENERAL_PURPOSE);
                transaction.setAmount(f.getAmount());
                transaction.setPaymentMethod(new HubPaymentMethod(f.getDocumentNumber()));
                transaction.setOwnerId(c.getId());

                transaction = transactionRepository.save(transaction);

                setRedirectToPath("/transaction/" + transaction.getId());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        model.addAttribute("form", new ClientTransactionForm());
        return "/transaction/edit";
    }

    public static class ClientTransactionForm {

        @Positive
        @NotNull
        private MonetaryAmount amount;

        @NotNull
        @DateTimeFormat(pattern = PATTERN_DATEPICKER_FORMAT)
        @PastOrPresent
        private LocalDate date;

        @NotBlank
        private String documentNumber;

        public MonetaryAmount getAmount() {
            return amount;
        }

        public void setAmount(MonetaryAmount amount) {
            this.amount = amount;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getDocumentNumber() {
            return documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
        }
    }
}
