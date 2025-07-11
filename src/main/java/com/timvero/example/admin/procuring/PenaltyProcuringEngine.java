package com.timvero.example.admin.procuring;

import static com.timvero.example.admin.procuring.ProcuringType.PENALTY;

import com.google.common.collect.MoreCollectors;
import com.timvero.application.procuring.ProcuringEngine;
import com.timvero.application.procuring.ProcuringType;
import com.timvero.application.procuring.entity.SecuredOffer;
import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.procuring.entity.PenaltySecuredOffer;
import com.timvero.ground.util.Lang;
import com.timvero.loan.offer.entity.ProductOffer;
import com.timvero.loan.product.entity.CreditProductAdditive;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PenaltyProcuringEngine implements ProcuringEngine {

    public ProcuringType procuringType() {
        return PENALTY;
    }

    @Override
    public Collection<? extends SecuredOffer> generateSecuredOffers(ProductOffer productOffer) {
        ExampleProductOffer preliminaryOffer = (ExampleProductOffer) productOffer;
        Participant participant = preliminaryOffer.getParticipant();

        return findOfferByAdditive(participant.getOffers(), productOffer.getProductAdditive()).stream()
            .map(correctedOffer -> createSecuredOffer(preliminaryOffer, correctedOffer))
            .toList();
    }

    private Optional<ExampleProductOffer> findOfferByAdditive(Collection<ExampleProductOffer> offers,
        CreditProductAdditive productAdditive) {

        return offers.stream()
            .filter(offer -> Objects.equals(productAdditive, offer.getProductAdditive()))
            .collect(MoreCollectors.toOptional());
    }

    private ExampleSecuredOffer createSecuredOffer(ExampleProductOffer preliminaryOffer,
        ExampleProductOffer correctedOffer) {

        PenaltySecuredOffer securedOffer = new PenaltySecuredOffer(preliminaryOffer);
        securedOffer.setMinAmount(Lang.max(preliminaryOffer.getMinAmount(), correctedOffer.getMinAmount()));
        securedOffer.setMaxAmount(Lang.min(preliminaryOffer.getMaxAmount(), correctedOffer.getMaxAmount()));
        securedOffer.setMinTerm(Lang.max(preliminaryOffer.getMinTerm(), correctedOffer.getMinTerm()));
        securedOffer.setMaxTerm(Lang.min(preliminaryOffer.getMaxTerm(), correctedOffer.getMaxTerm()));

        return securedOffer;
    }
}

