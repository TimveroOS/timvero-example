package com.timvero.example.admin.offer.entity;

import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_PRECISION;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_SCALE;

import com.timvero.application.procuring.ProcuringType;
import com.timvero.application.procuring.entity.SecuredOffer;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.ground.entity.NamedEntity;
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
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import javax.money.CurrencyUnit;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "secured_offer")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "procuring_type", discriminatorType = DiscriminatorType.STRING)
@Audited
@AuditOverride(forClass = SecuredOffer.class)
public class ExampleSecuredOffer extends SecuredOffer implements NamedEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "original_offer_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ExampleProductOffer originalOffer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Application application;

    @Column(name = "min_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "min_term", nullable = false)
    private Integer minTerm;

    @Column(name = "max_term", nullable = false)
    private Integer maxTerm;

    @Column(name = "procuring_type", insertable = false, updatable = false)
    private String procuringType;

    public ExampleSecuredOffer() {
    }

    public ExampleSecuredOffer(ExampleProductOffer originalOffer, ProcuringType procuringType) {
        this.originalOffer = originalOffer;
        this.application = originalOffer.getApplication();
        this.procuringType = procuringType.name();
    }

    @Transient
    public CurrencyUnit getCurrency() {
        return getOriginalOffer().getCreditProduct().getCurrency();
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(Integer minTerm) {
        this.minTerm = minTerm;
    }

    public Integer getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(Integer maxTerm) {
        this.maxTerm = maxTerm;
    }

    @Override
    public ExampleProductOffer getOriginalOffer() {
        return originalOffer;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String getOfferKey() {
        return getOriginalOffer().getUuid() + ":NO_PROCURING";
    }

    @Override
    public String getDisplayedName() {
        return null;
    }

    @Transient
    public ProcuringType getProcuringType() {
        return ProcuringType.valueOf(ProcuringType.class, procuringType);
    }
}
