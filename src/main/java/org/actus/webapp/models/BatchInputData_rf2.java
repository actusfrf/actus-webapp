package org.actus.webapp.models;

import java.util.List;
import java.util.Map;

public class BatchInputData_rf2 {
	
    private List<Map<String,Object>> contracts;
    private String scenarioID;

    public BatchInputData_rf2() {
    }
    
    public BatchInputData_rf2(List<Map<String,Object>> contracts, String scenarioID) {
        this.contracts = contracts;
        this.scenarioID = scenarioID;
    }
    
    public List<Map<String,Object>> getContracts() {
        return contracts;
    }

    public void setContracts(List<Map<String,Object>> contracts) {
        this.contracts = contracts;
    }

    public String getScenarioID() {
        return this.scenarioID;
    }

    public void setScenarioID(String scenarioID) {
        this.scenarioID = scenarioID;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActusData{");
        sb.append("contracts='").append(contracts).append('\'');
        sb.append(", scenarioID='").append(scenarioID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
