package org.actus.webapp.models;

import java.util.*;

public class EventStream2 {

    private String scenarioId;
    private String contractID;
    private String status;
    private String message;
    private List<Event> events;

    public EventStream2() {
    }

    public EventStream2(String scenarioId, String contractID, String status, String message, List<Event> events) {
        this.scenarioId = scenarioId;
        this.contractID = contractID;
        this.status = status;
        this.message = message;
        this.events = events;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventStream2{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append("contractID='").append(contractID).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", events='").append(events).append('\'');
        sb.append('}');
        return sb.toString();
    }
}