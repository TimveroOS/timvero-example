package com.timvero.example.admin.operation.charge;

import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

// tag::entity[]
@Entity
@DiscriminatorValue("901")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ChargeOperation extends CreditOperation {

    public static Integer TYPE = 901;

    @Embedded
    @NotNull
    private MonetaryAmount amount;

    protected ChargeOperation() {
        super();
    }

    public ChargeOperation(LocalDate date, MonetaryAmount amount) {
        super(TYPE, date, OperationStatus.APPROVED);
        this.amount = amount;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    @Override
    public boolean isEndDayOperation() {
        return false;
    }

    @Override
    public int getOrder() {
        return 101;
    }
}
// end::entity[]
