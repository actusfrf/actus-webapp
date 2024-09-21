package org.actus.webapp.models;

public class RiskFactorID_rf2 {
	// attributes
	private String riskFactorID;
	
	// null and useful constructors
	public RiskFactorID_rf2() {
    }
	public RiskFactorID_rf2(String riskFactorID) {
        this.riskFactorID = riskFactorID;
    }
	
	// get
    public String getRiskFactorID() {
        return this.riskFactorID;
    }
    // set
    public void setRiskFactorID(String riskFactorID) {
        this.riskFactorID = riskFactorID;
    }
    public String toString() {
    	return(getRiskFactorID());
    }
}
