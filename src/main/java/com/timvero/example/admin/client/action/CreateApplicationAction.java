package com.timvero.example.admin.client.action;

import com.timvero.example.admin.application.controller.ApplicationController;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.example.admin.application.form.ApplicationForm;
import com.timvero.example.admin.application.form.ApplicationFormMapper;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration;
import com.timvero.example.admin.participant.entity.Employment;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRole;
import com.timvero.example.admin.participant.entity.Periodicity;
import com.timvero.example.admin.participant.form.ParticipantFormMapper;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.signable.SignableDocumentService;
import com.timvero.web.common.action.EntityActionController;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/create-application")
public class CreateApplicationAction extends EntityActionController<UUID, Client, ApplicationForm> {

    @Autowired
    private ApplicationFormMapper mapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SignableDocumentService documentService;

    @Override
    protected EntityAction<Client, ApplicationForm> action() {
        return when(c -> c.getParticipants().stream().map(Participant::getApplication)
            .map(Application::getStatus).noneMatch(s -> s.equals(ApplicationStatus.NEW)))
            .then((client, form, user) -> {
                Application application = mapper.createEntity(form);
                application.getBorrowerParticipant().setClient(client);
                entityManager.persist(application);

                documentService.generate(application.getBorrowerParticipant(), ParticipantDocumentTypesConfiguration.APPLICATION_FORM);

                setRedirectToPath(ApplicationController.PATH + "/" + application.getId());
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        model.addAttribute("form", new ApplicationForm());
        model.addAttribute("isCreate", true);
        model.addAttribute("employmentTypes", Employment.values());
        model.addAttribute("periodicities", Periodicity.values());
        return "/application/edit";
    }

    @Override
    public String getHighlighted() {
        return BTN_LIGHT;
    }
}
