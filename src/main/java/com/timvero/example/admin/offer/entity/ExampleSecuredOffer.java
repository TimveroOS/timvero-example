package com.timvero.example.admin.offer.entity;

import com.timvero.application.procuring.ProcuringType;
import com.timvero.application.procuring.entity.SecuredOffer;
import com.timvero.ground.hibernate.type.CustomEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "secured_offer")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "procuring_type", discriminatorType = DiscriminatorType.STRING)
@Audited
@AuditOverride(forClass = SecuredOffer.class)
public abstract class ExampleSecuredOffer extends SecuredOffer {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "original_offer_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ExampleProductOffer originalOffer;

    @Column(name = "procuring_type", insertable = false, updatable = false)
    @Type(CustomEnumType.class)
    private ProcuringType procuringType;

    protected ExampleSecuredOffer() {
    }

    public ExampleSecuredOffer(ExampleProductOffer originalOffer, ProcuringType procuringType) {
        this.originalOffer = originalOffer;
        this.procuringType = procuringType;
    }

    @Override
    public ExampleProductOffer getOriginalOffer() {
        return originalOffer;
    }

    public ProcuringType getProcuringType() {
        return procuringType;
    }

    @Override
    public String getOfferKey() {
        return getOriginalOffer().getUuid() + ":NO_PROCURING";
    }
}
