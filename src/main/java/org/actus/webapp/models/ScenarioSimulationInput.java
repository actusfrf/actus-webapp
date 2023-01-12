package org.actus.webapp.models;

import java.util.*;
import java.time.LocalDateTime;

public class ScenarioSimulationInput {

    private String scenarioId;
    private List<Map<String,Object>> contracts;
    private LocalDateTime simulateTo;
    private Set<LocalDateTime> monitoringTimes;

    public ScenarioSimulationInput() {
    }

    public ScenarioSimulationInput(String scenarioId, List<Map<String,Object>> contracts, 
                                    LocalDateTime simulateTo, Set<LocalDateTime> monitoringTimes) {
        this.scenarioId = scenarioId;
        this.contracts = contracts;
        this.simulateTo = simulateTo;
        this.monitoringTimes = monitoringTimes;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public List<Map<String,Object>> getContracts() {
        return contracts;
    }

    public void setContracts(List<Map<String,Object>> contracts) {
        this.contracts = contracts;
    }

    public LocalDateTime getSimulateTo() {
        return this.simulateTo;
    }

    public void setSimulateTo(LocalDateTime simulateTo) {
        this.simulateTo = simulateTo;
    }

    public Set<LocalDateTime> getMonitoringTimes() {
        return this.monitoringTimes;
    }

    public void setMonitoringTimes(Set<LocalDateTime> monitoringTimes) {
        this.monitoringTimes = monitoringTimes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScenarioSimulationInput{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append(", contracts='").append(contracts).append('\'');
        sb.append(", simulateTo='").append(simulateTo).append('\'');
        sb.append(", monitoringTimes='").append(monitoringTimes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}