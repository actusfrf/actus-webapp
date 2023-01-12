package org.actus.webapp.models;

import java.util.List;

public class Margin {
    private Integer dimension;
    private List<Double> values;

    public Margin() {
    }

    public Margin(Integer dimension, List<Double> values) {
        this.dimension = dimension;
        this.values = values;
    }

    public Integer getDimension() {
        return this.dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public List<Double> getValues() {
        return this.values;
    }

    public void setValues(List<Double> values) {
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