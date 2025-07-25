package com.timvero.example.admin.client.action;

import com.timvero.example.admin.application.controller.ApplicationController;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.form.ApplicationForm;
import com.timvero.example.admin.application.form.ApplicationFormMapper;
import com.timvero.example.admin.application.service.ApplicationService;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.participant.entity.Employment;
import com.timvero.example.admin.participant.entity.Periodicity;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.EntityActionController;
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
    private ApplicationService applicationService;

    @Override
    protected EntityAction<Client, ApplicationForm> action() {
        return when(c -> true /*c.getParticipants().stream().map(Participant::getApplication)
            .map(Application::getStatus).noneMatch(s -> s.equals(ApplicationStatus.NEW))*/)
            .then((client, form, user) -> {
                Application application = mapper.createEntity(form);
                UUID applicationId = applicationService.createApplication(client, application);
                setRedirectToPath(ApplicationController.PATH + "/" + applicationId);
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
