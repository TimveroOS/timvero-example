package com.timvero.example.admin.product.repository;

import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleCreditProductRepository extends JpaRepository<ExampleCreditProduct, UUID> {

}
