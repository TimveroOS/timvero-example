package com.timvero.example.admin.document.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignableDocumentService;
import com.timvero.ground.document.signable.SignatureStatus;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/generate")
@Controller
@Order(2000)
public class GenerateDocumentAction extends SimpleActionController<UUID, SignableDocument> {

    @Autowired
    private SignableDocumentService documentService;
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super SignableDocument, Object> action() {
        return when(
            document -> document.getStatus().in(SignatureStatus.PENDING_SIGNATURE, SignatureStatus.GENERATION_FAILED))
            .then((document, f, u) -> {

                Participant participant = participantRepository.getReferenceById(document.getOwnerId());
                //if (document.getDocumentType() == APPLICATION_CONTRACT) {
                //    documentService.generate(participant, document.getDocumentType(),
                //        participant.getApplication().getCondition().getProductAdditive().getProduct()
                //            .getUuidContractTemplate());
                //} else {
                    documentService.generate(participant, document.getDocumentType());
                //}
            });
    }
}
