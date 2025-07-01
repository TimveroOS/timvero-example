package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.tab.AbstractFeaturesTab;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/features")
@Controller
@Order(9000)
public class ParticipantFeaturesTab extends AbstractFeaturesTab<Participant> {

}
