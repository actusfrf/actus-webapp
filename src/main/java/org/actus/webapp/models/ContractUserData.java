package org.actus.webapp.models;

import java.util.List;

public class ContractUserData {
	private String contractID;
	private List<String> modelIDs;
	
	public ContractUserData( ) {		
	}
	public ContractUserData( String contractID, List<String> modelIDs) {
		this.contractID = contractID;
		this.modelIDs = modelIDs;
	}
	
	public String getContractID() {
		return this.contractID;
	}
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}
	public List<String> getModelIDs(){
		return this.modelIDs ;
	}
	public void setModelIDs(List<String> modelIDs) {
		this.modelIDs = modelIDs;
	}
	// convert to string 
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContractUserData{");
        sb.append("contractID'").append(contractID).append('\'');
        sb.append(", modelIDs\'").append(modelIDs.toString());
        sb.append("\'}");
        return sb.toString();
    }
}
