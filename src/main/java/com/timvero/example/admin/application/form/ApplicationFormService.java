package com.timvero.example.admin.application.form;

import com.timvero.base.form.EntityFormService;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.entity.Employment;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.example.admin.participant.entity.Periodicity;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormService extends EntityFormService<Application, ApplicationForm, UUID> {

    @Override
    protected void assembleEditModel(Application entity, ApplicationForm form, Map<String, Object> model) {
        model.put("employmentTypes", Employment.values());
        model.put("periodicities", Periodicity.values());
    }

    @Override
    public boolean isEditable(Application entity) {
        return entity.getBorrowerParticipant().getStatus() == ParticipantStatus.NEW;
    }

}
