package com.timvero.example.admin.application.tab;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/schedule")
@Controller
@Order(5000)
public class ApplicationScheduleTab extends EntityTabController<UUID, Application> {

    @Override
    public boolean isVisible(Application application) {
        return application.getPaymentSchedule() != null;
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        Application application = loadEntity(id);
        model.addAttribute("schedule", application.getPaymentSchedule());
        return super.getTabTemplate(id, model);
    }
}
