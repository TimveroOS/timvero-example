package com.timvero.example.admin.participant.action;


import com.timvero.example.admin.offer.ProductOfferService;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantStatus;
import com.timvero.ground.action.EntityAction;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// tag::action[]
@RequestMapping("/generate-offers")
@Controller
public class GenerateOffersParticipantAction extends SimpleActionController<UUID, Participant> {

    @Autowired
    private ProductOfferService productOfferService;

    @Override
    protected EntityAction<? super Participant, Object> action() {
        return when(p -> isAvailable(p)).then((participant, f, u) -> {
            productOfferService.generateOffers(participant);
        });
    }

    private boolean isAvailable(Participant participant) {
        return participant.getStatus().in(ParticipantStatus.APPROVED)
            && (participant.getOffersGeneratedAt() == null || participant.getOfferGenerationException() != null);
    }

    @Override
    public String getHighlighted() {
        return BTN_PRIMARY;
    }
}
// end::action[]
