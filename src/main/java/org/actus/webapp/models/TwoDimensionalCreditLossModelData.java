package org.actus.webapp.models;

import java.util.List;

public class TwoDimensionalCreditLossModelData {
    private String riskFactorId;
    private String macroIndexId;
    private List<String> creditEventTimes;
    private List<Double> creditEventWeights;
    private TwoDimensionalSurfaceData surface;

    public TwoDimensionalCreditLossModelData() {
    }

    public TwoDimensionalCreditLossModelData(String riskFactorId, String macroIndexId, List<String> creditEventTimes, 
                                            List<Double> creditEventWeights, TwoDimensionalSurfaceData surface) {
        this.riskFactorId = riskFactorId;
        this.macroIndexId = macroIndexId;
        this.creditEventTimes = creditEventTimes;
        this.creditEventWeights = creditEventWeights;
        this.surface = surface;
    }

    public String getRiskFactorId() {
        return this.riskFactorId;
    }

    public void setRiskFactorId(String riskFactorId) {
        this.riskFactorId = riskFactorId;
    }

    public String getMacroIndexId() {
        return this.macroIndexId;
    }

    public void setMacroIndexId(String macroIndexId) {
        this.macroIndexId = macroIndexId;
    }


    public List<String> getCreditEventTimes() {
        return this.creditEventTimes;
    }

    public void setCreditEventTimes(List<String> creditEventTimes) {
        this.creditEventTimes = creditEventTimes;
    }

    public List<Double> getCreditEventWeights() {
        return this.creditEventWeights;
    }

    public void setCreditEventWeights(List<Double> creditEventWeights) {
        this.creditEventWeights = creditEventWeights;
    }

    public TwoDimensionalSurfaceData getSurface() {
        return this.surface;
    }

    public void setSurface(TwoDimensionalSurfaceData surface) {
        this.surface = surface;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TwoDimensionalCreditLossModelData{");
        sb.append(", riskFactorId='").append(riskFactorId).append('\'');
        sb.append(", macroIndexId='").append(macroIndexId).append('\'');
        sb.append(", creditEventTimes='").append(creditEventTimes).append('\'');
        sb.append(", creditEventWeights='").append(creditEventWeights).append('\'');
        sb.append(", surface='").append(surface).append('\'');
        sb.append('}');
        return sb.toString();
    }

}