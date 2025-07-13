package com.timvero.example.admin.operation.close;

import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.servicing.credit.entity.operation.AbstractOperationService;
import com.timvero.servicing.engine.general.CreditOperationHandler;
import com.timvero.servicing.engine.general.Snapshot;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CloseOperationService extends AbstractOperationService implements CreditOperationHandler<CloseOperation> {

    @Override
    public Class<CloseOperation> getOperationType() {
        return CloseOperation.class;
    }

    @Override
    public void apply(CloseOperation operation, Snapshot snapshot) {
        if (snapshot.debt.isEmpty()) {
            snapshot.status = CreditCalculationConfiguration.CLOSED;
        }
    }

    @Transactional
    public void createAndSaveOperation(UUID creditId, LocalDate date) {
        saveOperation(creditId, new CloseOperation(date));
    }
}
