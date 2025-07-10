package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.tab.AbstractPendingDecisionTab;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/execution")
@Controller
@Order(2000)
public class ParticipantExecutionTab extends AbstractPendingDecisionTab<Participant> {

    @Override
    public boolean isVisible(Participant entity) {
        return true;
    }
}
