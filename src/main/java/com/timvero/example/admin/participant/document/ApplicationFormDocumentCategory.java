package com.timvero.example.admin.participant.document;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.structure.template.entity.DocumentType;
import com.timvero.structure.template.generation.DocumentCategory;
import com.timvero.structure.template.generation.DocumentModel;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFormDocumentCategory extends DocumentCategory<UUID, Participant, DocumentModel> {

    public static final DocumentType TYPE = new DocumentType("APPLICATION_FORM");

    @Override
    public DocumentType getType() {
        return TYPE;
    }

    @Override
    protected DocumentModel getModel(Participant entity) {
        return new DocumentModel();
    }
}
