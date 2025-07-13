package com.timvero.example.admin.operation.voidCredit;

import com.timvero.servicing.credit.entity.operation.CreditOperation;
import com.timvero.servicing.credit.entity.operation.OperationStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import org.hibernate.envers.Audited;

@Entity
@DiscriminatorValue("995")
@Audited
public class VoidOperation extends CreditOperation {

    public static Integer TYPE = 995;

    protected VoidOperation() { }

    public VoidOperation(LocalDate date) {
        super(TYPE, date, OperationStatus.APPROVED);
    }

    @Override
    public boolean isEndDayOperation() {
        return false;
    }

    @Override
    public int getOrder() {
        return 995;
    }
}

