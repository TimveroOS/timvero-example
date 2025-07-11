package com.timvero.example.admin.offer;

import static com.timvero.example.admin.CustomConfiguration.PARTICIPANT_TREE;

import com.timvero.example.admin.participant.entity.Participant;
import com.timvero.flowable.internal.mapping.entity.DecisionProcessType;
import com.timvero.flowable.internal.service.ProcessExecutionService;
import com.timvero.loan.execution_result.ExecutionResultType;
import com.timvero.loan.offer.OfferEngineDataProcessor;
import com.timvero.loan.pending_decision.PendingDecision;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ExampleDataProcessor extends OfferEngineDataProcessor<UUID, Participant> {

    public static final ExecutionResultType TYPE = new ExecutionResultType("EXAMPLE");

    private final ProcessExecutionService processExecutionService;

    public ExampleDataProcessor(ProcessExecutionService processExecutionService) {
        this.processExecutionService = processExecutionService;
    }

    @Override
    public ExecutionResultType getResultType() {
        return TYPE;
    }

    @Override
    public Collection<Map<String, Object>> mapToData(Participant participant) {
        Map<String, Object> map = new HashMap<>();
        DecisionProcessType<Participant> processType = PARTICIPANT_TREE;
        map.putAll(processExecutionService.executionResult(participant, processType));
        map.putAll(participant.getPendingDecisions().stream()
            .filter(d -> d.getProcessGroup().equals(processType.name()))
            .map(PendingDecision::getValues)
            .map(Map::entrySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
        return List.of(map);
    }
}
