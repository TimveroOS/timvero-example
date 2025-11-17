package com.timvero.example.admin.application.action;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.form.ApplicationForm;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.web.common.action.EditEntityActionController;
import java.util.UUID;
import org.springframework.stereotype.Controller;

@Controller
public class EditApplicationAction extends EditEntityActionController<UUID, Application, ApplicationForm> {

    @Override
    protected boolean isOwnPage() {
        return false;
    }

    @Override
    public boolean isEditable(Application entity) {
        return entity.getBorrowerParticipant().getStatus() == ParticipantStatus.NEW;
    }

}
