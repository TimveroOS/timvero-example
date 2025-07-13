package com.timvero.example.admin.transaction.entity;

import com.timvero.base.entity.SynchronousAccessRepository;
import com.timvero.example.admin.credit.entity.ExampleCredit;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BorrowerTransactionRepository extends JpaRepository<BorrowerTransaction, Long>, JpaSpecificationExecutor<BorrowerTransaction>,
        SynchronousAccessRepository<BorrowerTransaction, Long> {

    Collection<BorrowerTransaction> findAllByOperation(CreditOperation operation);

    Collection<BorrowerTransaction> findAllByCredit(ExampleCredit credit);

    boolean existsByCreditId(UUID creditId);
}
