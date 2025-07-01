package com.timvero.example.admin.document.action;

import com.timvero.example.admin.document.signature.PhysicalDocumentSignature;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.EntityDocumentService;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignatureStatus;
import com.timvero.web.common.action.SimpleActionController;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sign")
@Controller
@Order(1000)
public class SignDocumentAction extends SimpleActionController<UUID, SignableDocument> {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EntityDocumentService entityDocumentService;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<SignableDocument, Object> action() {
        return when(d -> isRequiredDocAdded(d)
            && d.getStatus().in(SignatureStatus.PENDING_SIGNATURE) && d.getDocument() != null)

            .then((document, f, u) -> {
                Participant participant = participantRepository.getReferenceById(document.getOwnerId());

                PhysicalDocumentSignature signature = new PhysicalDocumentSignature();
                signature.setSigner(participant.getDisplayedName());
                document.setSignature(signature);

                document.setStatus(SignatureStatus.SIGNED);
            });
    }

    private boolean isRequiredDocAdded(SignableDocument d){
        Participant participant = participantRepository.getReferenceById(d.getOwnerId());
        return entityDocumentService.requiredDocumentsAdded(participant);
    }
}
