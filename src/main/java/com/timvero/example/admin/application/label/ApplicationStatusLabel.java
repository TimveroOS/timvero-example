package com.timvero.example.admin.application.label;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.ground.entity_marker.label.EntityStatusLabel;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStatusLabel extends EntityStatusLabel<Application> {

    public ApplicationStatusLabel() {
        super(Application::getStatus);
    }
}
