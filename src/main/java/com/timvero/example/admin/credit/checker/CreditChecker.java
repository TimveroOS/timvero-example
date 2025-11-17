package com.timvero.example.admin.credit.checker;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import com.timvero.ground.util.TransactionUtils;
import com.timvero.loan.metric.AnchoredMetricService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditChecker extends EntityChecker<ExampleCredit, UUID> {

    @Autowired
    private AnchoredMetricService anchoredMetricService;

    @Override
    protected void registerListeners(CheckerListenerRegistry<ExampleCredit> registry) {
        registry.entityChange().inserted();
    }

    @Override
    protected boolean isAvailable(ExampleCredit credit) {
        return true;
    }

    @Override
    protected void perform(ExampleCredit credit) {
        TransactionUtils.afterTransaction(() -> {
            anchoredMetricService.calculateAnchored(credit);
        });
    }
}