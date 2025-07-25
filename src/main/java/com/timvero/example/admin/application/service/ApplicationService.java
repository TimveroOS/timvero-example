package com.timvero.example.admin.application.service;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.application.entity.ApplicationRepository;
import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.participant.ParticipantDocumentTypesConfiguration;
import com.timvero.ground.document.signable.SignableDocumentService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SignableDocumentService documentService;

    @Transactional
    public UUID createApplication(Client client, Application application) {
        application.getBorrowerParticipant().setClient(client);
        
        Application savedApplication = applicationRepository.save(application);
        
        documentService.generate(savedApplication.getBorrowerParticipant(), 
                                ParticipantDocumentTypesConfiguration.APPLICATION_FORM);
        
        return savedApplication.getId();
    }
}
