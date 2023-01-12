package org.actus.webapp.models;

import java.util.List;

public class TermStructureData {
    private String riskFactorId;
    private String interpolationMethod; // e.g. "linear"
    private String extrapolationMethod; // e.g. "constant"
    private String dayCountConvention; // e.g. "A360"
    private String compoundingMethod; // e.g. "continuous"
    private String statusDate; // e.g. "2020-01-01T00:00:00"
    private List<Tenor> data;


    public TermStructureData() {
    }

    public TermStructureData(String riskFactorId, String interpolationMethod, String extrapolationMethod, String dayCountConvention, String compoundingMethod, String statusDate, List<Tenor> data) {
        this.riskFactorId = riskFactorId;
        this.interpolationMethod = interpolationMethod;
        this.extrapolationMethod = extrapolationMethod;
        this.dayCountConvention = dayCountConvention;
        this.compoundingMethod = compoundingMethod;
        this.statusDate = statusDate;
        this.data = data;
    }

    public String getRiskFactorId() {
        return this.riskFactorId;
    }

    public void setRiskFactorId(String riskFactorId) {
        this.riskFactorId = riskFactorId;
    }

    public String getInterpolationMethod() {
        return this.interpolationMethod;
    }

    public void setInterpolationMethod(String interpolationMethod) {
        this.interpolationMethod = interpolationMethod;
    }

    public String getExtrapolationMethod() {
        return this.extrapolationMethod;
    }

    public void setExtrapolationMethod(String extrapolationMethod) {
        this.extrapolationMethod = extrapolationMethod;
    }

    public String getDayCountConvention() {
        return this.dayCountConvention;
    }

    public void setDayCountConvention(String dayCountConvention) {
        this.dayCountConvention = dayCountConvention;
    }

    public String getCompoundingMethod() {
        return this.compoundingMethod;
    }

    public void setCompoundingMethod(String compoundingMethod) {
        this.compoundingMethod = compoundingMethod;
    }

    public String getStatusDate() {
        return this.statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public List<Tenor> getData() {
        return this.data;
    }

    public void setData(List<Tenor> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TermStructureData{");
        sb.append("riskFactorId='").append(riskFactorId).append('\'');
        sb.append(", interpolationMethod='").append(interpolationMethod).append('\'');
        sb.append(", extrapolationMethod='").append(extrapolationMethod).append('\'');
        sb.append(", dayCountConvention='").append(dayCountConvention).append('\'');
        sb.append(", compoundingMethod='").append(compoundingMethod).append('\'');
        sb.append(", statusDate='").append(statusDate).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }

}