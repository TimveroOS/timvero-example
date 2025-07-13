package com.timvero.example.admin.operation.charge;


import static com.timvero.example.admin.credit.CreditCalculationConfiguration.ACTIVE;
import static com.timvero.example.admin.credit.CreditCalculationConfiguration.PRINCIPAL;

import com.timvero.servicing.credit.entity.operation.AbstractOperationService;
import com.timvero.servicing.engine.general.CreditOperationHandler;
import com.timvero.servicing.engine.general.Snapshot;
import java.time.LocalDate;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.springframework.transaction.annotation.Transactional;

public class ChargeOperationService extends AbstractOperationService implements
    CreditOperationHandler<ChargeOperation> {


    public ChargeOperationService() {
        super();
    }

    @Override
    public Class<ChargeOperation> getOperationType() {
        return ChargeOperation.class;
    }


    @Transactional
    public ChargeOperation createOperation(UUID creditId, LocalDate date, MonetaryAmount amount) {
        return saveOperation(creditId, new ChargeOperation(date, amount));
    }

    @Override
    public void apply(ChargeOperation operation, Snapshot snapshot) {
        MonetaryAmount amount = operation.getAmount();
        snapshot.debt.computeIfAbsent(PRINCIPAL, amount.getCurrency()).add(amount);
        snapshot.status = ACTIVE;
    }
}
