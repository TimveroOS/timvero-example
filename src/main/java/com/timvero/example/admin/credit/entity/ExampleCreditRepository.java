package com.timvero.example.admin.credit.entity;

import com.timvero.base.entity.SynchronousAccessRepository;
import com.timvero.servicing.credit.entity.operation.CreditOperation;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExampleCreditRepository extends JpaRepository<ExampleCredit, UUID>,
        JpaSpecificationExecutor<ExampleCredit>, SynchronousAccessRepository<ExampleCredit, UUID> {

    @Query(nativeQuery = true, value = """
        select c.* from credit c\s
        join credit_condition cc on c.id = cc.condition
        join secured_offer so on so.id = cc.secured_offer_id
        join product_offer po on po.id = so.original_offer_id
        where po.product_additive = :additiveId
        """)
    Stream<ExampleCredit> getAllByAdditiveId(Long additiveId);

    ExampleCredit findByOperationsIn(CreditOperation... operation);
}
