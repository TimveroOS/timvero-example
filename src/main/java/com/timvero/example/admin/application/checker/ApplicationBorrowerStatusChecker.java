package com.timvero.example.admin.application.checker;

import static com.timvero.example.admin.application.entity.ApplicationStatus.IN_UNDERWRITING;
import static com.timvero.example.admin.application.entity.ApplicationStatus.VOID;
import static com.timvero.example.admin.participant.entity.ParticipantRole.BORROWER;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.Participant_;
import com.timvero.ground.checker.CheckerListenerRegistry;
import com.timvero.ground.checker.EntityChecker;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBorrowerStatusChecker extends EntityChecker<Application, UUID> {

    @Override
    protected void registerListeners(CheckerListenerRegistry<Application> registry) {
        registry.entityChange(Participant.class, Participant::getApplication).updated(Participant_.STATUS)
            .and(p -> p.getRoles().contains(BORROWER));
    }

    @Override
    protected boolean isAvailable(Application application) {
        return true;
    }

    @Override
    protected void perform(Application application) {
        switch (application.getBorrowerParticipant().getStatus()) {
            case IN_PROCESS: {
                application.setStatus(IN_UNDERWRITING);
                break;
            }
            case VOID: {
                application.setStatus(VOID);
                break;
            }
            default:
                // do nothing
        }
    }

}
