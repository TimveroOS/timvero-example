package com.timvero.example.admin.operation.close;

import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@DiscriminatorValue("999")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class CloseOperation extends CreditOperation {

    public static Integer TYPE = 999;

    protected CloseOperation() { }

    public CloseOperation(LocalDate date) {
        super(TYPE, date, OperationStatus.APPROVED);
    }

    @Override
    public boolean isEndDayOperation() {
        return false;
    }

    @Override
    public int getOrder() {
        return 999;
    }
}

