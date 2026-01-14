package com.timvero.example.admin.credit;

import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.entity.Credit;
import com.timvero.servicing.credit.entity.CreditSnapshot;
import com.timvero.servicing.credit.entity.debt.Debt;
import com.timvero.servicing.engine.AccrualService;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebtService {

    @Autowired
    private AccrualService accrualService;

    public Optional<Debt> getTotalDebt(ExampleCredit credit){
        Optional<Debt> snapshotDebt =
            Optional.ofNullable(credit.getActualSnapshot()).map(CreditSnapshot::getDebt);
        Optional<Debt> accrualDebt = Optional.of(accrualService.calculateCurrentAccurals(credit));
        return Stream.of(snapshotDebt, accrualDebt).filter(Optional::isPresent)
            .map(Optional::get).reduce(Debt::add);
    }

    public Optional<Debt> getTotalDebtByDate(Credit credit, LocalDate today){
        Optional<Debt> snapshotByDate = credit.getCreditSnapshot(today).map(CreditSnapshot::getDebt);

        Optional<Debt> accrualDebt = Optional.of(accrualService.calculateAccurals(credit, today));

        return Stream.of(snapshotByDate, accrualDebt)
            .flatMap(Optional::stream)
            .reduce(Debt::add)
            .filter(d -> !d.isEmpty());
    }
}
