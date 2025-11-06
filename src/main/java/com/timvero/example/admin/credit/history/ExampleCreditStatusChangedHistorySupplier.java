package com.timvero.example.admin.credit.history;

import com.timvero.servicing.credit.entity.CreditSnapshot;
import com.timvero.servicing.credit.history.CreditActualSnapshotHistorySupplier;
import org.springframework.stereotype.Component;

@Component
public class ExampleCreditStatusChangedHistorySupplier extends CreditActualSnapshotHistorySupplier {

    public static final String CREDIT_STATUS_CHANGED = "CREDIT_STATUS_CHANGED";

    @Override
    public String getEventPage() {
        return "/history/fragments/credit/credit_status_changed.html";
    }

    @Override
    public String getEventTitle() {
        return CREDIT_STATUS_CHANGED;
    }

    @Override
    protected boolean shouldCreateHistoryEvent(CreditSnapshot beforeSnapshot, CreditSnapshot afterSnapshot) {
        return !beforeSnapshot.getStatus().equals(afterSnapshot.getStatus());
    }
}