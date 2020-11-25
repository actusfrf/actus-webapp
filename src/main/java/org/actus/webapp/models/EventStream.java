package org.actus.webapp.models;

import java.util.*;

public class EventStream {

    private String contractId;
    private String status;
    private String message;
    private List<Event> events;

    public EventStream() {
    }

    public EventStream(String contractId, String status, String message, List<Event> events) {
        this.contractId = contractId;
        this.status = status;
        this.message = message;
        this.events = events;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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
        final StringBuilder sb = new StringBuilder("EventStream{");
        sb.append("contractId='").append(contractId).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", events='").append(events).append('\'');
        sb.append('}');
        return sb.toString();
    }
}