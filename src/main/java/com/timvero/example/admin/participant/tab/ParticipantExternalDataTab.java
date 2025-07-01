package com.timvero.example.admin.participant.tab;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.loan.risk.tab.EntityExternalDataTabController;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/external-data")
@Controller
@Order(8000)
public class ParticipantExternalDataTab extends EntityExternalDataTabController<Participant> {

    @Override
    public TabZone getTabZone() {
        return TabZone.PRIMARY;
    }
}
