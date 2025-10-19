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

@RequestMapping("/covenant-result-archive")
@Controller
@Order(10500)
public class CreditCovenantResultArchiveTab extends AbstractCovenantResultTab<ExampleCredit> {

    @Autowired
    private CovenantResultRepository covenantResultRepository;

    @Override
    protected List<CovenantResult> getCovenantResults(UUID id) {
        return covenantResultRepository.findAllByOwnerId(id);
    }
}
