package org.actus.webapp.models;

import java.util.List;

public class TwoDimensionalPrepaymentModelData {
    private String riskFactorId;
    private String referenceRateId;
    private List<String> prepaymentEventTimes;
    private TwoDimensionalSurfaceData surface;

    public TwoDimensionalPrepaymentModelData() {
    }

    public TwoDimensionalPrepaymentModelData(String riskFactorId, String referenceRateId, 
                                            List<String> prepaymentEventTimes, TwoDimensionalSurfaceData surface) {
        this.riskFactorId = riskFactorId;
        this.referenceRateId = referenceRateId;
        this.prepaymentEventTimes = prepaymentEventTimes;
        this.surface = surface;
    }

    public String getRiskFactorId() {
        return this.riskFactorId;
    }

    public void setRiskFactorId(String riskFactorId) {
        this.riskFactorId = riskFactorId;
    }

    public String getReferenceRateId() {
        return this.referenceRateId;
    }

    public void setReferenceRateId(String referenceRateId) {
        this.referenceRateId = referenceRateId;
    }

    public List<String> getPrepaymentEventTimes() {
        return this.prepaymentEventTimes;
    }

    public void setPrepaymentEventTimes(List<String> prepaymentEventTimes) {
        this.prepaymentEventTimes = prepaymentEventTimes;
    }

    public TwoDimensionalSurfaceData getSurface() {
        return this.surface;
    }

    public void setSurface(TwoDimensionalSurfaceData surface) {
        this.surface = surface;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TwoDimensionalPrepaymentModelData{");
        sb.append(", riskFactorId='").append(riskFactorId).append('\'');
        sb.append(", referenceRateId='").append(referenceRateId).append('\'');
        sb.append(", prepaymentEventTimes='").append(prepaymentEventTimes).append('\'');
        sb.append(", surface='").append(surface).append('\'');
        sb.append('}');
        return sb.toString();
    }

}