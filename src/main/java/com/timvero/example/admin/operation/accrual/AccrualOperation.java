package com.timvero.example.admin.operation.accrual;

import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

// tag::entity[]
@Entity
@DiscriminatorValue("950")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class AccrualOperation extends CreditOperation {

    public static Integer TYPE = 950;

    protected AccrualOperation() {
        super();
    }

    public AccrualOperation(LocalDate date) {
        super(TYPE, date, OperationStatus.APPROVED);
    }

    @Override
    public boolean isEndDayOperation() {
        return false;
    }

    @Override
    public int getOrder() {
        return 111;
    }

}
// end::entity[]
