package com.timvero.example.admin.credit.tab;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.loan.covenantexecution.entity.CovenantResult;
import com.timvero.loan.covenantexecution.entity.CovenantResultRepository;
import com.timvero.loan.covenantexecution.tab.AbstractCovenantResultTab;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/covenant-result")
@Controller
@Order(9500)
public class CreditCovenantResultTab extends AbstractCovenantResultTab<ExampleCredit> {

    @Autowired
    private CovenantResultRepository covenantResultRepository;

    @Override
    protected List<CovenantResult> getCovenantResults(UUID id) {
        return covenantResultRepository.findLatestByOwnerGroupedBySubjectId(id);
    }
}
