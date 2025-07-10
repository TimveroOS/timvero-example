package com.timvero.example.admin.offer.entity;

import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_PRECISION;
import static com.timvero.ground.hibernate.type.MonetaryAmountType.AMOUNT_SCALE;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.ground.entity.NamedEntity;
import com.timvero.ground.util.EntityUtils;
import com.timvero.loan.offer.entity.ProductOffer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.util.UUID;
import javax.money.CurrencyUnit;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "product_offer")
@Audited
@AuditOverride(forClass = ProductOffer.class)
public class ExampleProductOffer extends ProductOffer implements NamedEntity {

    @NotAudited
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "uuid default gen_random_uuid()")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Participant participant;

    @Column(name = "min_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE, nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "min_term", nullable = false)
    private Integer minTerm;

    @Column(name = "max_term", nullable = false)
    private Integer maxTerm;

    public ExampleProductOffer() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
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

    @PrePersist
    protected void prePersist() {
        uuid = UUID.randomUUID();
    }

    @Transient
    public Application getApplication() {
        return participant != null ? participant.getApplication() : null;
    }

    @Override
    @Transient
    public ExampleCreditProduct getCreditProduct() {
        return (ExampleCreditProduct) getProductAdditive().getProduct();
    }

    @Override
    public ExampleCreditProductAdditive getProductAdditive() {
        return (ExampleCreditProductAdditive) EntityUtils.initializeAndUnproxy(super.getProductAdditive());
    }

    @Transient
    public CurrencyUnit getCurrency() {
        return getCreditProduct().getCurrency();
    }

    @Transient
    @Override
    public String getDisplayedName() {
        return getCreditProduct().getDisplayedName();
    }

    @Override
    public String toString() {
        return "VisionProductOffer ["
            + "minAmount=" + minAmount + ", "
            + "maxAmount=" + maxAmount + ", "
            + "minTerm=" + minTerm + ", "
            + "maxTerm=" + maxTerm + ", "
            + "currency=" + getCurrency().getCurrencyCode() + ", "
            + "additive=" + getProductAdditive().toString() + ", "
            + "product=" + getCreditProduct().getDisplayedName() + "]";
    }
}
