package org.actus.webapp.models;

import java.util.List;

public class Scenario_rf2 {
	// attributes
	private String scenarioID;
	private List<RiskFactorID_rf2> marketRiskFactorIDs;
	
	// null and useful constructors
	public Scenario_rf2() {
    }
	
	public Scenario_rf2(String scenarioID, List<RiskFactorID_rf2> marketRiskFactorIDs) {
        this.scenarioID = scenarioID;
        this.marketRiskFactorIDs = marketRiskFactorIDs;
    }
	
	// get for each attribute
    public String getScenarioID() {
        return this.scenarioID;
    }
    public List<RiskFactorID_rf2> getMarketRiskFactorIDs(){ 
    	return this.marketRiskFactorIDs;
    }
	
    // set for each attribute 
    public void setString(String scenarioID) {
        this.scenarioID = scenarioID;
    }
    public void setList(List<RiskFactorID_rf2> marketRiskFactorIDs) {
    	this.marketRiskFactorIDs = marketRiskFactorIDs;
    }
    
    public String toJSON() {
    	String outStr = "{\"scenarioID\": \""+scenarioID+"\", \"marketRiskFactorIDs\":[" ;
    	boolean first=true;
    	for (RiskFactorID_rf2 rfid : this.marketRiskFactorIDs) {
    		if (!first) {
    	        outStr += ", ";
    			first = false;
    		} 
    		outStr+= "{\"riskFactorID\": \"" + rfid.getRiskFactorID() + "\"}";
    	}
    	outStr += "]}";
    	return(outStr);
    }

}
