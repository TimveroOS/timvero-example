package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.web.common.tab.EntityHistoryTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

// tag::class[]
@Controller
@Order(10000)
public class ParticipantHistoryTab extends EntityHistoryTabController<UUID, Participant> {

}
// end::class[]
