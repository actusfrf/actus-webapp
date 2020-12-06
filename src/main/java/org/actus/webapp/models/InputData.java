package org.actus.webapp.models;

import java.util.*;

public class InputData {

    private Map<String,Object> contract;
    private List<ObservedData> riskFactors;

    public InputData() {
    }

    public InputData(Map<String,Object> contract, List<ObservedData> riskFactors) {
        this.contract = contract;
        this.riskFactors = riskFactors;
    }

    public Map<String,Object> getContract() {
        return contract;
    }

    public void setContract(Map<String,Object> contract) {
        this.contract = contract;
    }

    public List<ObservedData> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<ObservedData> riskFactors) {
        this.riskFactors = riskFactors;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InputData{");
        sb.append("contract='").append(contract).append('\'');
        sb.append(", riskFactors='").append(riskFactors).append('\'');
        sb.append('}');
        return sb.toString();
    }
}