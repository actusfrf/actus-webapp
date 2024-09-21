package org.actus.webapp.models;

import java.util.List;

public class BatchStartInput {
	private  String scenarioID;
	private  List<ContractUserData> contractUserData;
    // may eventually want to pass unparsed list for each contract - list of ACTUS terms 
	
	public BatchStartInput(){
	}
	public BatchStartInput(String scenarioID, List<ContractUserData> contractUserData) {
		this.scenarioID = scenarioID;
		this.contractUserData = contractUserData;
	}
	public String getScenarioID() {
		return this.scenarioID;
	}
	public void  putScenarioID(String scenarioID) {
		this.scenarioID = scenarioID;
	}
	public List<ContractUserData> getContractUserData(){
		return this.contractUserData;
	}
	public void setContractUserData(List<ContractUserData> contractUserData) {
		this.contractUserData = contractUserData;
	}
	// convert to string 
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BatchStartInput{");
        sb.append("scenarioID'").append(scenarioID).append("\'");
        sb.append(",").append(contractUserData.toString());
        sb.append('}');
        return sb.toString();
    }

}
