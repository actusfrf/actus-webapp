package com.bundiconsulting.actuswebapp.models;

import java.time.LocalDateTime;
import java.util.Map;

import org.actus.events.ContractEvent;
//import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.JsonNode;


public class Event {
    String type;
    String time;
    double payoff;
    String currency;
    double nominalValue;
    double nominalRate;
    double nominalAccrued;
    
    
    // for deserialization 
    public Event() {
    }

    public Event(ContractEvent event) {
    	this.type = event.type();
    	this.time = event.time().toString();
    	this.payoff = event.payoff();
    	this.currency = event.currency();
    	this.nominalValue = event.states()[1];
    	this.nominalRate = event.states()[3];
    	this.nominalAccrued = event.states()[2];
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getPayoff() {
		return payoff;
	}

	public void setPayoff(double payoff) {
		this.payoff = payoff;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getNominalValue() {
		return nominalValue;
	}

	public void setNominalValue(double nominalValue) {
		this.nominalValue = nominalValue;
	}

	public double getNominalRate() {
		return nominalRate;
	}

	public void setNominalRate(double nominalRate) {
		this.nominalRate = nominalRate;
	}

	public double getNominalAccrued() {
		return nominalAccrued;
	}

	public void setNominalAccrued(double nominalAccrued) {
		this.nominalAccrued = nominalAccrued;
	}

    
}