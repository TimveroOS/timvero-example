package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.tab.AbstractProcessExecutionTab;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/process-execution")
@Controller
@Order(3200)
public class ParticipantProcessExecutionTab extends AbstractProcessExecutionTab<Participant> {

}
