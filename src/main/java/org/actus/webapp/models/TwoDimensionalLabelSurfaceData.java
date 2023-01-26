package org.actus.webapp.models;

import java.util.List;
import org.actus.webapp.models.LabelMargin;

public class TwoDimensionalLabelSurfaceData {
    private String interpolationMethod; // e.g. "linear"
    private String extrapolationMethod; // e.g. "constant"
    private List<LabelMargin> labelMargins;
    private List<List<Double>> data;

    public TwoDimensionalLabelSurfaceData() {
    }

    public TwoDimensionalLabelSurfaceData(String interpolationMethod, 
             String extrapolationMethod, List<LabelMargin> labelMargins, List<List<Double>> data) {
        this.interpolationMethod = interpolationMethod;
        this.extrapolationMethod = extrapolationMethod;
        this.labelMargins = labelMargins;
        this.data = data;
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

    public List<LabelMargin> getLabelMargins() {
        return this.labelMargins;
    }

    public void setLabelMargins(List<LabelMargin> labelMargins) {
        this.labelMargins = labelMargins;
    }

    public List<List<Double>> getData() {
        return this.data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TwoDimensionalSurfaceData{");
        sb.append(", interpolationMethod='").append(interpolationMethod).append('\'');
        sb.append(", extrapolationMethod='").append(extrapolationMethod).append('\'');
        sb.append(", labelMargins='").append(labelMargins).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
