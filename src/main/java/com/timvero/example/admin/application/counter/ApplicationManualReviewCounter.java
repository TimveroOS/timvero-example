package com.timvero.example.admin.application.counter;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationStatus;
import com.timvero.ground.entity_marker.counter.EntityCounter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2000)
public class ApplicationManualReviewCounter implements EntityCounter<Application> {

    @Override
    public boolean isEntityMarked(Application entity) {
        return entity.getStatus() == ApplicationStatus.MANUAL_REVIEW;
    }

    @Override
    public String getName() {
        return "applicationManualReview";
    }
}
