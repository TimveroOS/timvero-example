package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.web.common.tab.EntityDocumentTabController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

@Controller
@Order(1500)
public class ParticipantDocumentsTab extends EntityDocumentTabController<Participant> {

    @Override
    public boolean isVisible(Participant entity) {
        return true;
    }
}
