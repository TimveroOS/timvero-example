package com.timvero.example.portal.application;

import static com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration.APPLICATION_CONTRACT;
import static com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration.APPLICATION_FORM;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationRepository;
import com.timvero.example.admin.application.service.ApplicationService;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.portal.application.form.CreateApplicationMapper;
import com.timvero.example.portal.application.form.CreateApplicationRequest;
import com.timvero.example.portal.client.ClientService;
import com.timvero.example.portal.exception.PreconditionFailedException;
import com.timvero.ground.document.exception.SignatureException;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.ground.document.signable.SignableDocumentService;
import com.timvero.ground.document.signable.SignableDocumentType;
import com.timvero.ground.filter.base.NotFoundException;
import com.timvero.integration.docusign.DocusignSignatureService;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationPortalService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CreateApplicationMapper createApplicationMapper;
    @Autowired
    private ApplicationService applicationService;
    @Autowired(required = false)
    private DocusignSignatureService docusignSignatureService;
    @Autowired
    private SignableDocumentService signableDocumentService;

    @Transactional
    public UUID createApplication(CreateApplicationRequest request) {
        Client client = clientService.getById(request.getClientId());
        Application application = createApplicationMapper.toApplication(request);

        return applicationService.createApplication(client, application);
    }

    @Transactional(readOnly = true)
    public PortalApplicationStatus getApplicationStatus(UUID id) {
        return applicationRepository.findStatusById(id)
            .map(PortalApplicationStatus::fromApplicationStatus)
            .orElseThrow(() -> new NotFoundException("Application not found with ID: " + id));
    }

    @Transactional
    public String getSignatureUrl(UUID applicationId, String returnUrl) throws IOException, SignatureException {
        if (docusignSignatureService == null) {
            throw new SignatureException("DocuSign is not enabled");
        }

        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new NotFoundException("Application not found with ID: " + applicationId));

        PortalApplicationStatus portalStatus = PortalApplicationStatus.fromApplicationStatus(application.getStatus());

        SignableDocumentType documentType;
        switch (portalStatus) {
            case IN_PROCESS -> documentType = APPLICATION_FORM;
            case PENDING_CONTRACT_SIGNATURE -> documentType = APPLICATION_CONTRACT;
            case null, default -> throw new PreconditionFailedException(
                "Signature is not available for application status: " + portalStatus);
        }

        Participant participant = application.getBorrowerParticipant();
        SignableDocument document = signableDocumentService.getAllDocuments(participant).stream()
            .filter(doc -> documentType.getDocumentType().equals(doc.getDocumentType().getDocumentType()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException(
                "Not found signable document of type " + documentType.getDocumentType()
                    + " for application with ID: " + applicationId
            ));

        return docusignSignatureService.getDocusignUrl(participant, document, returnUrl);
    }

}
