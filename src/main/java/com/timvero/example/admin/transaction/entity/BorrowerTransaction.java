package com.timvero.example.admin.transaction.entity;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.transfer.method.entity.PaymentMethod;
import com.timvero.transfer.transaction.entity.PaymentTransaction;
import com.timvero.transfer.transaction.entity.TransactionType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import javax.money.MonetaryAmount;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@DiscriminatorValue("BORROWER")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class BorrowerTransaction extends PaymentTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ExampleCredit credit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    @NotAudited
    private CreditOperation operation;

    public BorrowerTransaction() {}

    public BorrowerTransaction(TransactionType type, MonetaryAmount amount, PaymentMethod paymentMethod,
        ExampleCredit credit) {
        setType(type);
        setAmount(amount);
        this.credit = credit;
        setPaymentMethod(paymentMethod);
    }

    public ExampleCredit getCredit() {
        return credit;
    }

    public CreditOperation getOperation() {
        return operation;
    }

    public void setOperation(CreditOperation operation) {
        this.operation = operation;
    }

    @Override
    public String getOrderId() {
        return getCredit().getId().toString();
    }

}
