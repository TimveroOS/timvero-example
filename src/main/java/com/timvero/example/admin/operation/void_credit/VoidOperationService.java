package com.timvero.example.admin.operation.void_credit;

import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.servicing.credit.entity.operation.AbstractOperationService;
import com.timvero.servicing.engine.general.CreditOperationHandler;
import com.timvero.servicing.engine.general.Snapshot;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoidOperationService extends AbstractOperationService implements CreditOperationHandler<VoidOperation> {

    @Override
    public Class<VoidOperation> getOperationType() {
        return VoidOperation.class;
    }

    @Override
    public void apply(VoidOperation operation, Snapshot snapshot) {
        snapshot.status = CreditCalculationConfiguration.VOID;
    }

    @Transactional
    public void createAndSaveOperation(UUID creditId, LocalDate date) {
        VoidOperation voidOperation = new VoidOperation(date);
        saveOperation(creditId, voidOperation);
    }
}
