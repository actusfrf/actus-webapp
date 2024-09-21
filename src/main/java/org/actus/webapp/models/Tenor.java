package org.actus.webapp.models;

import java.time.Period;

public class Tenor {
    private Period tenor;
    private Double value;

    public Tenor() {
    }

    public Tenor(Period tenor, Double value) {
        this.tenor = tenor;
        this.value = value;
    }

    public Period getTenor() {
        return tenor;
    }

    public void setTime(Period tenor) {
        this.tenor = tenor;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tenor{");
        sb.append("tenor='").append(tenor).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }

}