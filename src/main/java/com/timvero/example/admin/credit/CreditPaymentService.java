package com.timvero.example.admin.credit;

import com.timvero.example.admin.credit.entity.ExampleCreditRepository;
import com.timvero.ground.util.Lang;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.operation.CreditPayment;
import com.timvero.servicing.engine.CreditCalculationService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditPaymentService {

    @Autowired
    private ExampleCreditRepository creditRepository;

    @Autowired
    private CreditCalculationService calculationService;

    //@todo it breaks action  @Transactional(propagation = Propagation.MANDATORY)
    public Credit getCreditForPayment(CreditPayment payment) {
        return creditRepository.findByOperationsIn(payment);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean movePaymentDate(CreditPayment payment, LocalDate newDate) {
        Credit credit = creditRepository.findByOperationsIn(payment);
        LocalDate calcDate = Lang.min(payment.getDate(), newDate);
        payment.setDate(newDate);
        calculationService.calculate(credit.getId(), calcDate, credit.getCalculationDate());
        return true;
    }
}
