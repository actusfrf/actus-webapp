package org.actus.webapp.models;

import java.util.*;

public class ObservedData {

    private String marketObjectCode;
    private Double base;
    private List<DataPoint> data; 

    public ObservedData() {
    }

    public ObservedData(String marketObjectCode, Double base, List<DataPoint> data) {
        this.marketObjectCode = marketObjectCode;
        this.base = base;
        this.data = data;
    }

    public String getMarketObjectCode() {
        return marketObjectCode;
    }

    public void setMarketObjectCode(String marketObjectCode) {
        this.marketObjectCode = marketObjectCode;
    }

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public List<DataPoint> getData() {
        return data;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ObservedData{");
        sb.append("marketObjectCode='").append(marketObjectCode).append('\'');
        sb.append(", base='").append(base).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}