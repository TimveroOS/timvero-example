package com.timvero.example.admin.procuring;

import static com.timvero.example.admin.procuring.ExampleProcuringType.PENALTY;

import com.timvero.application.procuring.ProcuringEngine;
import com.timvero.application.procuring.ProcuringType;
import com.timvero.application.procuring.entity.SecuredOffer;
import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.procuring.entity.PenaltySecuredOffer;
import com.timvero.loan.offer.entity.ProductOffer;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PenaltyProcuringEngine implements ProcuringEngine {

    @Override
    public ProcuringType procuringType() {
        return PENALTY;
    }

    @Override
    public Collection<? extends SecuredOffer> generateSecuredOffers(ProductOffer productOffer) {
        return List.of(new PenaltySecuredOffer((ExampleProductOffer) productOffer));
    }
}

