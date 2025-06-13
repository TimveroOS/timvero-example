package com.timvero.example.admin.participant.controller;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.ground.filter.base.ListFilter;
import com.timvero.web.common.ViewableFilterController;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("participant")
public class ParticipantController extends ViewableFilterController<UUID, Participant, ListFilter> {

    @Override
    protected String getHeaderPage() {
        return "/participant/header";
    }

    @Override
    protected String getViewPage() {
        return "/participant/view";
    }
}