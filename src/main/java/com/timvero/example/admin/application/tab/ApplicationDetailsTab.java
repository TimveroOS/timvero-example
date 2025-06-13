package com.timvero.example.admin.application.tab;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/details")
@Controller
@Order(1000)
public class ApplicationDetailsTab extends EntityTabController<UUID, Application> {

    @Override
    public boolean isVisible(Application application) {
        return true;
    }
}