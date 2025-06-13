package com.timvero.example.admin.application.action;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.form.ApplicationForm;
import com.timvero.web.common.action.EditEntityActionController;
import java.util.UUID;
import org.springframework.stereotype.Controller;

@Controller
public class EditApplicationAction extends EditEntityActionController<UUID, Application, ApplicationForm> {

}
