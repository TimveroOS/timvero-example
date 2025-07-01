package com.timvero.example.admin.document.action;

import com.timvero.ground.action.EntityAction;
import com.timvero.ground.document.signable.SignableDocument;
import com.timvero.web.common.action.EntityActionController;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/delete-signable-uploaded-document")
@Controller
@Order(4000)
public class DeleteSignableUploadedDocumentAction extends EntityActionController<UUID, SignableDocument, Object> {

    @Override
    public String getHighlighted() {
        return BTN_SUCCESS;
    }

    @Override
    protected EntityAction<? super SignableDocument, Object> action() {
        return when(d -> !d.isSigned() && d.getDocument() != null)
            .then((d, f, u) -> d.setDocument(null));
    }

    @Override
    protected String getActionTemplate(UUID id, Model model, String actionPath) throws Exception {
        model.addAttribute("title", "document.dialog.action.delete.title");
        model.addAttribute("message", "document.dialog.action.delete.message");
        return "/common/action/yes-no";
    }
}
