package com.timvero.example.admin.operation.payment;

import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@DiscriminatorValue("200")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ExampleCreditPayment extends CreditPayment {

    public static Integer TYPE = 200;

    public ExampleCreditPayment(LocalDate date, MonetaryAmount amount) {
        super(TYPE, date, OperationStatus.APPROVED, amount);
    }

    protected ExampleCreditPayment() {
    }

    @Override
    public Integer getType() {
        return TYPE;
    }

    @Override
    public int getOrder() {
        return 200;
    }
}