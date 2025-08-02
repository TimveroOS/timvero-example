package com.timvero.example.admin.application;

import com.timvero.application.procuring.entity.SecuredOffer;
import com.timvero.application.procuring.view.OfferView;
import com.timvero.application.procuring.view.ProductOfferViewService;
import com.timvero.example.admin.offer.entity.ExampleProductOffer;
import com.timvero.example.admin.offer.entity.ExampleSecuredOffer;
import com.timvero.ground.tools.EnumUtils;
import com.timvero.ground.util.MonetaryUtil;
import com.timvero.loan.offer.entity.ProductOffer;
import java.util.Locale;
import org.springframework.stereotype.Service;


// tag::view[]
@Service
public class ExampleProductOfferViewService extends ProductOfferViewService {

    @Override
    protected OfferView map(ProductOffer productOffer, Locale locale) {
        ExampleProductOffer offer = (ExampleProductOffer) productOffer;
        String details = EnumUtils.getLocalizedValue(offer.getProductAdditive().getProcuringType(), locale);

        return new OfferView(offer.getProductAdditive().getName(),
            offer.getProductAdditive().getInterestRate().toString(),
            offer.getProductAdditive().getInterestRate().toString(),
            MonetaryUtil.of(offer.getMinAmount(), offer.getCurrency()),
            MonetaryUtil.of(offer.getMaxAmount(), offer.getCurrency()), offer.getMinTerm(), offer.getMaxTerm(),
            details);
    }

    @Override
    protected OfferView map(SecuredOffer securedOffer, Locale locale) {
        ExampleSecuredOffer offer = (ExampleSecuredOffer) securedOffer;
        ExampleProductOffer originalOffer = offer.getOriginalOffer();
        String details = EnumUtils.getLocalizedValue(offer.getProcuringType(), locale);
        return new OfferView(originalOffer.getProductAdditive().getName(),
            originalOffer.getProductAdditive().getInterestRate().toString(),
            originalOffer.getProductAdditive().getInterestRate().toString(),
            MonetaryUtil.of(originalOffer.getMinAmount(), originalOffer.getCurrency()),
            MonetaryUtil.of(originalOffer.getMaxAmount(), originalOffer.getCurrency()), originalOffer.getMinTerm(), originalOffer.getMaxTerm(),
            details);
    }
}
// end::view[]
