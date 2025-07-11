package com.timvero.example.admin.offer;

import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.product.entity.ExampleCreditProduct;
import com.timvero.example.admin.product.repository.ExampleCreditProductRepository;
import com.timvero.loan.offer.OfferEngine;
import com.timvero.loan.offer.exception.OfferGenerationException;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOfferService {

    private final OfferEngine offerEngine;
    private final ExampleDataProcessor exampleDataProcessor;
    private final ExampleCreditProductRepository exampleCreditProductRepository;
    private final ExampleOfferRepository exampleOfferRepository;

    public ProductOfferService(ExampleCreditProductRepository exampleCreditProductRepository, OfferEngine offerEngine,
        ExampleDataProcessor exampleDataProcessor, ExampleOfferRepository exampleOfferRepository) {
        this.exampleCreditProductRepository = exampleCreditProductRepository;
        this.offerEngine = offerEngine;
        this.exampleDataProcessor = exampleDataProcessor;
        this.exampleOfferRepository = exampleOfferRepository;
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.MANDATORY)
    public void generateOffers(Participant participant) {
        List<ExampleCreditProduct> products = exampleCreditProductRepository.findAll();
        try {
            Collection<ExampleProductOffer> offers = (Collection<ExampleProductOffer>)
                offerEngine.generateOffers(exampleDataProcessor, products, participant);

            offers.forEach(offer -> offer.setParticipant(participant));
            exampleOfferRepository.saveAll(offers);
        } catch (OfferGenerationException e) {
            //TODO handle exception
        }
    }
}
