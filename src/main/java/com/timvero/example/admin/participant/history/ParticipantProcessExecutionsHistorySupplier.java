package com.timvero.example.admin.participant.history;

import com.timvero.base.audit.HistoryEvent;
import com.timvero.base.audit.HistoryEvent.Icon;
import com.timvero.base.audit.HistoryEventSupplier;
import com.timvero.base.entity.Auditor;
import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.execution.ProcessExecution;
import com.timvero.flowable.internal.execution.ProcessExecutionStatus;
import com.timvero.flowable.internal.fact.DecisionFactService;
import com.timvero.flowable.internal.fact.DecisionFactService.DecisionFactDTO;
import com.timvero.flowable.internal.service.ProcessExecutionService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// tag::class[]
@Component
public class ParticipantProcessExecutionsHistorySupplier implements
    HistoryEventSupplier<Participant, ParticipantProcessExecutionsHistorySupplier.ParticipantProcessExecutionHistoryEvent> {

    @Autowired
    private DecisionFactService decisionFactService;
    @Autowired
    private ProcessExecutionService processExecutionService;

    public static final String PARTICIPANT_PROCESS_EXECUTION = "PARTICIPANT_PROCESS_EXECUTION";

    @Override
    public Class<Participant> getRootClass() {
        return Participant.class;
    }

    @Override
    public String getEventPage() {
        return "/history/fragments/participant/participant_process_execution";
    }

    @Override
    public List<ParticipantProcessExecutionHistoryEvent> getHistoryEvents(Participant participant) {
        return processExecutionService.getAllRealExecutions(participant.getId())
            .filter(p -> p.getStatus() == ProcessExecutionStatus.COMPLETED)
            .map(processExecution -> {
                Map<String, List<DecisionFactDTO>> decisionFacts = decisionFactService
                    .getExecutionFacts(processExecution.getId()).stream()
                    .collect(Collectors.groupingBy(DecisionFactDTO::value));

                return createEvent(processExecution, decisionFacts);
            })
            .toList();
    }

    private ParticipantProcessExecutionHistoryEvent createEvent(ProcessExecution processExecution,
        Map<String, List<DecisionFactDTO>> decisionFacts) {
        return new ParticipantProcessExecutionHistoryEvent(
            Icon.EDIT,
            PARTICIPANT_PROCESS_EXECUTION,
            processExecution.getCreatedAt(),
            null,
            processExecution,
            decisionFacts
        );
    }

    public static class ParticipantProcessExecutionHistoryEvent extends HistoryEvent {

        private final ProcessExecution processExecution;
        private final Map<String, List<DecisionFactDTO>> decisionFacts;

        public ParticipantProcessExecutionHistoryEvent(Icon icon, String type, Instant date, Auditor auditor,
            ProcessExecution processExecution, Map<String, List<DecisionFactDTO>> decisionFacts) {
            super(icon, type, date, auditor);
            this.processExecution = processExecution;
            this.decisionFacts = decisionFacts;
        }

        public ProcessExecution getProcessExecution() {
            return processExecution;
        }

        public Map<String, List<DecisionFactDTO>> getDecisionFacts() {
            return decisionFacts;
        }
    }
}
// end::class[]
