package org.actus.webapp.models;
import org.actus.events.ContractEvent;
import org.springframework.data.mongodb.core.mapping.Field;


public class Event {
	@Field
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
    	this.type = event.eventType().toString();
    	this.time = event.eventTime().toString();
    	this.payoff = event.payoff();
    	this.currency = event.currency();
    	this.nominalValue = event.states().notionalPrincipal;
    	this.nominalRate = event.states().nominalInterestRate;
    	this.nominalAccrued = event.states().accruedInterest;
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