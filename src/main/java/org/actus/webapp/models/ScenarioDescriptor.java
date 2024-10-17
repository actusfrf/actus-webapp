package org.actus.webapp.models;

public class ScenarioDescriptor {
	// attributes
	private String scenarioID;     // key into scenario.scenario store
	private String scenarioType;   // extensible set of rf types (market, prepaymentModel) for now 
	
	public ScenarioDescriptor() {	
	}
	
	public ScenarioDescriptor(String scenarioID, String scenarioType) {
		this.scenarioID   = scenarioID;
		this.scenarioType = scenarioType;
	}
	
	public String getScenarioID() {
		return this.scenarioID;
	}
	void setScenarioID(String scenarioID) {
		this.scenarioID = scenarioID;
	}
	public String getScenarioType() {
		return this.scenarioType;
	}
	void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}
	
	public String toString() {
    	String str = "{ \"scenarioID\": \"" +
          this.scenarioID +  "\", \"scenarioType\": " +
          this.scenarioType + " }";
    	return(str);  
    }
}
