package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.tab.AbstractProfileTab;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/profile")
@Controller
@Order(3000)
public class ParticipantProfileTab extends AbstractProfileTab<Participant> {

}