package com.timvero.example.admin.participant.checker;


import static com.timvero.example.admin.participant.entity.ParticipantStatus.APPROVED;

import com.timvero.example.admin.offer.ProductOfferService;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.Participant_;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GenerateOffersChecker extends EntityChecker<Participant, UUID> {

    private final ProductOfferService productOfferService;

    public GenerateOffersChecker(ProductOfferService productOfferService) {
        this.productOfferService = productOfferService;
    }

    @Override
    protected void registerListeners(CheckerListenerRegistry<Participant> registry) {
        registry.entityChange().updated(Participant_.STATUS);
    }

    @Override
    protected boolean isAvailable(Participant participant) {
        return participant.getStatus().in(APPROVED);
    }

    @Override
    protected void perform(Participant participant) {
        productOfferService.generateOffers(participant);
    }
}
