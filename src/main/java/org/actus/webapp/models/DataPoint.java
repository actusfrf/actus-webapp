package org.actus.webapp.models;

public class DataPoint {
    private String time;
    private Double value;

    public DataPoint() {
    }

    public DataPoint(String time, Double value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataPoint{");
        sb.append("time='").append(time).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }

}