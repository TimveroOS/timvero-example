package com.timvero.example.admin.document.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.exception.SignatureException;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignatureStatus;
import com.timvero.integration.docusign.DocusignSignatureService;
import com.timvero.web.common.action.SimpleActionController;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/docusign")
@Controller
@Order(1900)
@ConditionalOnBean(DocusignSignatureService.class)
public class DocusignAction extends SimpleActionController<UUID, SignableDocument> {

    @Autowired
    private DocusignSignatureService signatureService;
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super SignableDocument, Object> action() {
        return when(document -> document.getStatus().in(SignatureStatus.PENDING_SIGNATURE))
            .then((document, f, u) -> {

                Participant participant = participantRepository.getReferenceById(document.getOwnerId());
                try {
                    signatureService.sign(participant, document);
                } catch (IOException | SignatureException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
