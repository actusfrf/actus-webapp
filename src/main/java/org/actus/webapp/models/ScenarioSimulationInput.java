package org.actus.webapp.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScenarioSimulationInput {

    private String scenarioID;
    private List<Map<String,Object>> contracts;
    private LocalDateTime simulateTo;
    private Set<LocalDateTime> monitoringTimes;

    public ScenarioSimulationInput() {
    }

    public ScenarioSimulationInput(String scenarioID, List<Map<String,Object>> contracts, 
                                    LocalDateTime simulateTo, Set<LocalDateTime> monitoringTimes) {
        this.scenarioID = scenarioID;
        this.contracts = contracts;
        this.simulateTo = simulateTo;
        this.monitoringTimes = monitoringTimes;
    }

    public String getScenarioID() {
        return scenarioID;
    }

    public void setScenarioId(String scenarioID) {
        this.scenarioID = scenarioID;
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
        sb.append("scenarioId='").append(scenarioID).append('\'');
        sb.append(", contracts='").append(contracts).append('\'');
        sb.append(", simulateTo='").append(simulateTo).append('\'');
        sb.append(", monitoringTimes='").append(monitoringTimes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}