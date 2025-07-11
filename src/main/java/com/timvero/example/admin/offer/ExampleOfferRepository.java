package com.timvero.example.admin.offer;

import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleOfferRepository extends JpaRepository<ExampleProductOffer, Long> {
}
