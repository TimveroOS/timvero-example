package com.timvero.example.admin.participant;

import com.timvero.example.admin.application.entity.Application;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.example.admin.participant.entity.ParticipantRepository;
import com.timvero.ground.tools.EnumUtils;
import com.timvero.loan.launchpad.DecisionEntityExtractor;
import com.timvero.loan.launchpad.LaunchpadEntityDto;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ParticipantLaunchpadService implements DecisionEntityExtractor {

    @Autowired
    private ParticipantRepository repository;

    @Override
    public String getDecisionOwnerType() {
        return Participant.DECISION_OWNER_TYPE;
    }

    @Override
    public LaunchpadEntityDto getByHolderId(Long holderId) {
        Participant participant = repository.findByPendingDecisionHolderId(holderId)
            .orElseThrow(() -> new RuntimeException("No target entity with holder id " + holderId));
        Application application = participant.getApplication();

        String roles = participant.getRoles().stream()
            .map(role -> EnumUtils.getLocalizedValue(role, LocaleContextHolder.getLocale()))
            .collect(Collectors.joining(", "));

        return new LaunchpadEntityDto(participant.getId(), application, application.getDisplayedName(),
            participant, participant.getDisplayedName(), roles,
            "/participant/launchpad-body");
    }
}
