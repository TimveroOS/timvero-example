package com.timvero.example.admin.product.repository;

import com.timvero.example.admin.product.entity.ExampleCreditProductAdditive;
import com.timvero.loan.product.entity.CreditProduct;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleCreditProductAdditiveRepository extends JpaRepository<ExampleCreditProductAdditive, Long> {

    Collection<ExampleCreditProductAdditive> findAllByProduct(CreditProduct entity);
}
