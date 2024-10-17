package org.actus.webapp.models;

import java.util.List;
import java.util.Map;

public class BatchInputData_rf2 {
	
    private List<Map<String,Object>> contracts;
    private ScenarioDescriptor scenarioDescriptor;

    public BatchInputData_rf2() {
    }
    
    public BatchInputData_rf2(List<Map<String,Object>> contracts,
    						  ScenarioDescriptor scenarioDescriptor) {
        this.contracts = contracts;
        this.scenarioDescriptor = scenarioDescriptor;
    }
    
    public List<Map<String,Object>> getContracts() {
        return contracts;
    }

    public void setContracts(List<Map<String,Object>> contracts) {
        this.contracts = contracts;
    }

    public ScenarioDescriptor getScenarioDescriptor() {
        return this.scenarioDescriptor;
    }

    public void setScenarioDescriptor(ScenarioDescriptor scenarioDescriptor) {
        this.scenarioDescriptor = scenarioDescriptor;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActusData{");
        sb.append("contracts='").append(contracts).append('\'');
        sb.append(", scenarioID='").append(scenarioDescriptor.toString()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
