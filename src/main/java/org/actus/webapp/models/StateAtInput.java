package org.actus.webapp.models;

import java.time.LocalDateTime;

public class StateAtInput {
	private String id;
	private LocalDateTime time;
	
	public StateAtInput() {
	}
    public StateAtInput( String id, LocalDateTime time) {
    	this.id = id;
    	this.time = time;
    }
    public String getId() {
    	return id;
    }
    public void put(String id) {
    	this.id = id;
    }
    public LocalDateTime getTime() {
    	return time;
    }
    public void setTime(LocalDateTime time) {
    	this.time = time;
    }
}
