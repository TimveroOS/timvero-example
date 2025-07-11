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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOfferService {

    private final OfferEngine offerEngine;
    private final ExceptionEntityService exceptionService;
    private final ExampleDataProcessor exampleDataProcessor;
    private final ExampleCreditProductRepository exampleCreditProductRepository;
    private final EntityManager entityManager;

    public ProductOfferService(ExampleCreditProductRepository exampleCreditProductRepository, OfferEngine offerEngine,
        ExceptionEntityService exceptionService, ExampleDataProcessor exampleDataProcessor,
        EntityManager entityManager) {
        this.exampleCreditProductRepository = exampleCreditProductRepository;
        this.offerEngine = offerEngine;
        this.exceptionService = exceptionService;
        this.exampleDataProcessor = exampleDataProcessor;
        this.entityManager = entityManager;
    }

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
