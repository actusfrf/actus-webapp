package org.actus.webapp.models;

import org.actus.states.StateSpace;
public class BehaviorStateAtInput {
	private String riskFactorId ;
	private StateSpace  states;
	
	public BehaviorStateAtInput() {		
	}
	public BehaviorStateAtInput(String riskFactorId, StateSpace states) {
		this.riskFactorId = riskFactorId;
		this.states = states;
	}
	
	public String getRiskFactorId() {
		return this.riskFactorId;
	}
	public void setRiskFactorId(String riskFactorId) {
		this.riskFactorId = riskFactorId;
	}
	public StateSpace getStates() {
		return this.states;
	}
	public void setStates(StateSpace states) {
		this.states = states;
	}
	 @Override
	public String toString() {
	final StringBuilder sb = new StringBuilder("BehaviorStateAtInput{");
	        sb.append("riskFactorId='").append(riskFactorId).append('\'');
	        sb.append(", states='").append(states).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }  
}
