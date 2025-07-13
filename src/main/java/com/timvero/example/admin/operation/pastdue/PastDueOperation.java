package com.timvero.example.admin.operation.pastdue;

import com.timvero.example.admin.scheduled.ExampleCreditCondition;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@DiscriminatorValue("900")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PastDueOperation extends CreditOperation {

    public static Integer TYPE = 900;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ExampleCreditCondition condition;

    private Boolean maturity;

    protected PastDueOperation() {
        super();
    }

    public PastDueOperation(LocalDate date, ExampleCreditCondition condition, boolean maturity) {
        super(TYPE, date, OperationStatus.APPROVED);
        this.condition = condition;
        this.maturity = maturity;
    }

    public ExampleCreditCondition getCondition() {
        return condition;
    }

    public Boolean isMaturity() {
        return maturity;
    }

    @Override
    public boolean isEndDayOperation() {
        return true;
    }

    @Override
    public int getOrder() {
        return 900;
    }
}
