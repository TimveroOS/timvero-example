package com.timvero.example.admin.participant.checker;


import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.Participant_;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GenerateOffersChecker extends EntityChecker<Participant, UUID> {

  /*  @Autowired
    private ProductOfferService productOfferService;*/

    @Override
    protected void registerListeners(CheckerListenerRegistry<Participant> registry) {
        registry.entityChange().updated(Participant_.STATUS);
    }

    @Override
    protected boolean isAvailable(Participant participant) {
        return true;
     /*   return participant.getStatus().in(APPROVED)
            && participant.getApplication().getApplicationType().in(PRE_APPROVED, VENDOR);*/
    }

    @Override
    protected void perform(Participant participant) {
        return;
        /*if(participant.getApplication().getApplicationType() == PRE_APPROVED) {
            productOfferService.generatePreApprovedOffers(participant);
        }else if (participant.getApplication().getApplicationType() == VENDOR) {
            productOfferService.generateVendorOffers(participant);
        }*/
    }
}
