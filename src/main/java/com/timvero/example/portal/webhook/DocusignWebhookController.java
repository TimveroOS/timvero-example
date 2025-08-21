package com.timvero.example.portal.webhook;

import com.timvero.ground.document.exception.SignatureException;
import com.timvero.integration.docusign.DocusignSignatureService;
import com.timvero.integration.docusign.DocusignWebhookResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = DocusignWebhookController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class DocusignWebhookController {

    protected static final String PATH = "/callback/docusign";

    @Value("${docusign.user.id}")
    private String userId;

    @Autowired
    private DocusignSignatureService docusignSignatureService;

    @PostMapping(value = "/webhook", produces = "application/json;charset=UTF-8")
    public void handleWebhook(@RequestBody DocusignWebhookResponse payload) throws IOException {
        if (payload.getData() != null) {
            if (payload.getData().getUserId().equals(userId)) {
                String envelopeId = payload.getData().getEnvelopeId();
                try {
                    docusignSignatureService.signDocumentByEnvelopeId(envelopeId);
                } catch (SignatureException e) {
                 // ignore
                }
            }
        }
    }
}
