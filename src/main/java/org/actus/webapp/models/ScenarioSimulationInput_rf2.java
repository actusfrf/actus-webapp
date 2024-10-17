package org.actus.webapp.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScenarioSimulationInput_rf2 {

    private ScenarioDescriptor scenarioDescriptor;
    private List<Map<String,Object>> contracts;
    private LocalDateTime simulateTo;
    private Set<LocalDateTime> monitoringTimes;

    public ScenarioSimulationInput_rf2() {
    }

    public ScenarioSimulationInput_rf2(ScenarioDescriptor scenarioDescriptor, List<Map<String,Object>> contracts, 
                                    LocalDateTime simulateTo, Set<LocalDateTime> monitoringTimes) {
        this.scenarioDescriptor = scenarioDescriptor;
        this.contracts = contracts;
        this.simulateTo = simulateTo;
        this.monitoringTimes = monitoringTimes;
    }

    public ScenarioDescriptor getScenarioDescriptor() {
        return this.scenarioDescriptor;
    }

    public void setScenarioDescriptor(ScenarioDescriptor scenarioDescriptor) {
        this.scenarioDescriptor = scenarioDescriptor;
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
        sb.append("scenarioDescriptor='").append(scenarioDescriptor.toString()).append('\'');
        sb.append(", contracts='").append(contracts).append('\'');
        sb.append(", simulateTo='").append(simulateTo).append('\'');
        sb.append(", monitoringTimes='").append(monitoringTimes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}