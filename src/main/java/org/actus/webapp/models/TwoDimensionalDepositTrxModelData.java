package org.actus.webapp.models;

import java.util.List;

public class TwoDimensionalDepositTrxModelData {
    private String riskFactorId;
    private List<String> depositTrxEventTimes;
    private TwoDimensionalLabelSurfaceData labelSurface;

    public TwoDimensionalDepositTrxModelData() {
    }

    public TwoDimensionalDepositTrxModelData(String riskFactorId, List<String> depositTrxEventTimes,
   					     TwoDimensionalLabelSurfaceData labelSurface) {
        this.riskFactorId = riskFactorId;
        this.depositTrxEventTimes = depositTrxEventTimes;
        this.labelSurface = labelSurface;
    }

    public String getRiskFactorId() {
        return this.riskFactorId;
    }

    public void setRiskFactorId(String riskFactorId) {
        this.riskFactorId = riskFactorId;
    }

    public List<String> getDepositTrxEventTimes() {
        return this.depositTrxEventTimes;
    }

    public void setDepositTrxEventTimes(List<String> depositTrxEventTimes) {
        this.depositTrxEventTimes = depositTrxEventTimes;
    }

    public TwoDimensionalLabelSurfaceData getLabelSurface() {
        return this.labelSurface;
    }

    public void setLabelSurface(TwoDimensionalLabelSurfaceData labelSurface) {
        this.labelSurface = labelSurface;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TwoDimensionalPrepaymentModelData{");
        sb.append(", riskFactorId='").append(riskFactorId).append('\'');
        sb.append(", depositTrxEventTimes'").append(depositTrxEventTimes).append('\'');
        sb.append(", labelSurface='").append(labelSurface).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
