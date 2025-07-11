package com.timvero.example.admin.application.tab;


import com.timvero.application.procuring.view.ProductOfferViewService;
import com.timvero.example.admin.application.entity.Application;
import com.timvero.web.common.tab.EntityTabController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/offers")
@Controller
@Order(4000)
public class ApplicationOffersTab extends EntityTabController<UUID, Application> {

    private final ProductOfferViewService productOfferViewService;

    public ApplicationOffersTab(ProductOfferViewService productOfferViewService) {
        this.productOfferViewService = productOfferViewService;
    }

    @Override
    public boolean isVisible(Application application) {
        return !application.getBorrowerParticipant().getOffers().isEmpty();
    }

    @Override
    protected String getTabTemplate(UUID id, Model model) throws Exception {
        Application application = loadEntity(id);
        model.addAttribute("allOffers",
            productOfferViewService.allOffers(application.getBorrowerParticipant().getOffers(), null));

        return super.getTabTemplate(id, model);
    }
}
