package com.timvero.example.admin.transaction.entity;

import com.timvero.ground.hibernate.type.EnumCodeType;
import com.timvero.transfer.method.entity.PaymentMethod;
import com.timvero.transfer.transaction.entity.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import org.hibernate.annotations.Type;

@Entity
@DiscriminatorValue(LiquidityClientPaymentMethod.TYPE)
public class LiquidityClientPaymentMethod extends PaymentMethod {

    public static final String TYPE = LiquidityPaymentGateway.GATEWAY_TYPE;

    @Column(name = "processed_date")
    private LocalDate processedDate;

    @Embedded
    private MonetaryAmount amount;

    @Column(name = "name")
    private String ownerName;

    @Type(EnumCodeType.class)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    protected LiquidityClientPaymentMethod() {
    }

    public LiquidityClientPaymentMethod(LocalDate processedDate, MonetaryAmount amount, TransactionType transactionType,
        String ownerName) {
        super(TYPE);
        this.processedDate = processedDate;
        this.amount = amount;
        this.transactionType = transactionType;
        this.ownerName = ownerName;
    }


    public LocalDate getProcessedDate() {
        return processedDate;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

}
