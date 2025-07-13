package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.CreditCalculationConfiguration;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.example.admin.operation.accrual.RecordedAccrualEngine;
import com.timvero.example.admin.operation.accrual.RecordedAccrualEngine.AccrualRecord;
import com.timvero.ground.util.MonetaryUtil;
import com.timvero.servicing.engine.distribution.DistributionService;
import com.timvero.servicing.engine.distribution.SnapshotRecord;
import com.timvero.web.common.tab.EntityTabController;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/credit-accruals")
@Controller
@Order(10002)
public class CreditAccrualsTab extends EntityTabController<UUID, ExampleCredit> {

    @Autowired
    private List<RecordedAccrualEngine> accrualEngineList;

    @Autowired
    private DistributionService distributionService;

    @Override
    public boolean isVisible(ExampleCredit entity) {
        return entity.getActualSnapshot() != null && entity.getActualSnapshot().getStatus() != CreditCalculationConfiguration.VOID;
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        ExampleCredit credit = loadEntity(id);
        model.addAttribute("credit", credit);
        NavigableMap<LocalDate, SnapshotRecord> snapshotRecords = distributionService.getSnapshotRecords(credit);

        Map<String, List<AccrualRecord>> accrualsMap = new HashMap<>();
        for (RecordedAccrualEngine accrualEngine : accrualEngineList) {
            accrualsMap.put(accrualEngine.getAccrualAccount(),
                accrualEngine.calculateAccrualRecords(credit, snapshotRecords, credit.getCalculationDate()).stream()
                    .sorted(Comparator.comparing(AccrualRecord::start))
                    .toList());
        }
        MonetaryAmount zero = MonetaryUtil.zero(credit.getCondition().getPrincipal().getCurrency());
        HashMap<String, MonetaryAmount> totals = new HashMap<>();
        accrualsMap.entrySet().stream()
                .forEach(e -> {
                    MonetaryAmount total = e.getValue().stream()
                        .map(AccrualRecord::increment)
                        .reduce(MonetaryAmount::add)
                        .orElse(zero);
                    totals.put(e.getKey(), total);
                });

        model.addAttribute("totals", totals);
        model.addAttribute("accruals", accrualsMap);

        return super.getTabTemplate(id, model);
    }
}
