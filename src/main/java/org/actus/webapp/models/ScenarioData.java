package org.actus.webapp.models;

import java.util.*;

public class ScenarioData {

    private String scenarioId;
    private List<ObservedData> timeSeriesData;
    private List<TermStructureData> termStructureData;
    private List<TwoDimensionalPrepaymentModelData> twoDimensionalPrepaymentModelData;
    private List<TwoDimensionalCreditLossModelData> twoDimensionalCreditLossModelData;
    private List<TwoDimensionalDepositTrxModelData> twoDimensionalDepositTrxModelData;
    
    public ScenarioData() {
    }

    public ScenarioData(String scenarioId, List<ObservedData> timeSeriesData, List<TermStructureData> termStructureData, 
                        List<TwoDimensionalPrepaymentModelData> twoDimensionalPrepaymentModelData,
                        List<TwoDimensionalCreditLossModelData> twoDimensionalCreditLossModelData,
                        List<TwoDimensionalDepositTrxModelData> twoDimensionalDepositTrxModelData) {
        this.scenarioId = scenarioId;
        this.timeSeriesData = timeSeriesData;
        this.termStructureData = termStructureData;
        this.twoDimensionalPrepaymentModelData = twoDimensionalPrepaymentModelData;
        this.twoDimensionalCreditLossModelData = twoDimensionalCreditLossModelData;
        this.twoDimensionalDepositTrxModelData = twoDimensionalDepositTrxModelData;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public List<ObservedData> getTimeSeriesData() {
        return timeSeriesData;
    }

    public void setTimeSeriesData(List<ObservedData> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }

    public List<TermStructureData> getTermStructureData() {
        return termStructureData;
    }

    public void setTermStructureData(List<TermStructureData> termStructureData) {
        this.termStructureData = termStructureData;
    }

    public List<TwoDimensionalPrepaymentModelData> getTwoDimensionalPrepaymentModelData() {
        return twoDimensionalPrepaymentModelData;
    }

    public void setTwoDimensionalPrepaymentModelData(List<TwoDimensionalPrepaymentModelData> twoDimensionalPrepaymentModelData) {
        this.twoDimensionalPrepaymentModelData = twoDimensionalPrepaymentModelData;
    }

    public List<TwoDimensionalCreditLossModelData> getTwoDimensionalCreditLossModelData() {
        return twoDimensionalCreditLossModelData;
    }

    public void setTwoDimensionalCreditLossModelData(List<TwoDimensionalCreditLossModelData> twoDimensionalCreditLossModelData) {
        this.twoDimensionalCreditLossModelData = twoDimensionalCreditLossModelData;
    }
    
    public List<TwoDimensionalDepositTrxModelData> getTwoDimensionalDepositTrxModelData() {
        return twoDimensionalDepositTrxModelData;
    }

    public void setTwoDimensionalDepositTrxModelData(List<TwoDimensionalDepositTrxModelData> twoDimensionalDepositTrxModelData) {
        this.twoDimensionalDepositTrxModelData = twoDimensionalDepositTrxModelData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScenarioData{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append(", timeSeriesData='").append(timeSeriesData).append('\'');
        sb.append(", termStructureData='").append(termStructureData).append('\'');
        sb.append(", twoDimensionalPrepaymentModelData='").append(twoDimensionalPrepaymentModelData).append('\'');
        sb.append(", twoDimensionalCreditLossModelData='").append(twoDimensionalCreditLossModelData).append('\'');
        sb.append(", twoDimensionalDepositTrxModelData='").append(twoDimensionalDepositTrxModelData).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
