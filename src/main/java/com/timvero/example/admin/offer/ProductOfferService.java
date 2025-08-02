package com.timvero.example.admin.offer;

import com.timvero.base.exception.ExceptionEntity;
import com.timvero.base.exception.ExceptionEntityService;
import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.repository.ExampleCreditProductRepository;
import com.timvero.loan.offer.OfferEngine;
import com.timvero.loan.offer.exception.OfferGenerationException;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// tag::service[]
@Service
public class ProductOfferService {

    @Autowired
    private OfferEngine offerEngine;
    @Autowired
    private ExceptionEntityService exceptionService;
    @Autowired
    private ExampleDataProcessor exampleDataProcessor;
    @Autowired
    private ExampleCreditProductRepository exampleCreditProductRepository;
    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.MANDATORY)
    public void generateOffers(Participant participant) {
        List<ExampleCreditProduct> products = exampleCreditProductRepository.findAll();

        try {
            Collection<ExampleProductOffer> offers = (Collection<ExampleProductOffer>) offerEngine
                .generateOffers(exampleDataProcessor, products, participant);

            offers.forEach(offer -> offer.setParticipant(participant));
            offers.forEach(entityManager::persist);

            participant.setOfferGenerationException(null);
        } catch (OfferGenerationException exception) {
            ExceptionEntity exceptionEntity = exceptionService.saveException(exception, "OfferGenerator");
            participant.setOfferGenerationException(exceptionEntity);
        } finally {
            participant.setOffersGeneratedAt(Instant.now());
        }
    }
}
// end::service[]
