package com.timvero.example.admin.procuring.entity;

import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.example.admin.procuring.ProcuringType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "penalty_secured_offer")
@DiscriminatorValue(ProcuringType.CODE_PENALTY)
@Audited
public class PenaltySecuredOffer extends ExampleSecuredOffer {

    public PenaltySecuredOffer() {
    }

    public PenaltySecuredOffer(ExampleProductOffer originalOffer) {
        super(originalOffer, ProcuringType.PENALTY);
    }

    @Override
    public String getOfferKey() {
        return getOriginalOffer().getUuid() + ":PENALTY";
    }

    @Override
    public String getDisplayedName() {
        return getOriginalOffer().getDisplayedName();
    }
}
