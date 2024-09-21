package org.actus.webapp.models;

import java.util.List;

public class LabelMargin {
    private Integer dimension;
    private List<String> values;

    public LabelMargin() {
    }

    public LabelMargin(Integer dimension, List<String> values) {
        this.dimension = dimension;
        this.values = values;
    }

    public Integer getDimension() {
        return this.dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }    

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Margin{");
        sb.append("dimension='").append(dimension).append('\'');
        sb.append(", values='").append(values).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
