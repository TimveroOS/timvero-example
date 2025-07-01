package com.timvero.example.admin.document.action;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignableDocumentService;
import com.timvero.ground.document.signable.SignatureStatus;
import com.timvero.ground.service.storage.DocumentResource;
import com.timvero.web.common.action.EntityActionController;
import com.timvero.web.common.action.form.UploadFileForm;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/upload")
@Controller
@Order(3000)
public class UploadSignableAction extends EntityActionController<UUID, SignableDocument, UploadFileForm> {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private SignableDocumentService signableDocumentService;

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super SignableDocument, UploadFileForm> action() {
        return when(d -> d.getStatus().in(SignatureStatus.PENDING_SIGNATURE))
            .then((d, f, u) -> {
                Participant participant = participantRepository.getReferenceById(d.getOwnerId());
                try {
                    DocumentResource documentResource = new DocumentResource(f.getFile().getOriginalFilename(),
                        f.getFile().getContentType(), f.getFile().getBytes());
                    signableDocumentService.saveUploadDocument(participant, d, documentResource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        return "/common/action/upload";
    }
}
