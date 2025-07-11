package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/offers")
@Controller
@Order(7000)
public class ParticipantOffersTab extends EntityTabController<UUID, Participant> {

    @Override
    public boolean isVisible(Participant entity) {
        return entity.getOffersGeneratedAt() != null;
    }
}
