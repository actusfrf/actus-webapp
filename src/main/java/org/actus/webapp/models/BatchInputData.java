package org.actus.webapp.models;

import java.util.*;

public class BatchInputData {

    private List<Map<String,Object>> contracts;
    private List<ObservedData> riskFactors;

    public BatchInputData() {
    }

    public BatchInputData(List<Map<String,Object>> contracts, List<ObservedData> riskFactors) {
        this.contracts = contracts;
        this.riskFactors = riskFactors;
    }

    public List<Map<String,Object>> getContracts() {
        return contracts;
    }

    public void setContracts(List<Map<String,Object>> contracts) {
        this.contracts = contracts;
    }

    public List<ObservedData> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<ObservedData> riskFactors) {
        this.riskFactors = riskFactors;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActusData{");
        sb.append("contracts='").append(contracts).append('\'');
        sb.append(", riskFactors='").append(riskFactors).append('\'');
        sb.append('}');
        return sb.toString();
    }
}