package org.actus.webapp.models;

public class CalloutData {
	private String modelID;
	private String time;       // in format yyyy:mm:ddT00:00;00 
	
	public CalloutData() {
	}
	public CalloutData(String modelID, String time) {
		this.modelID = modelID;
		this.time    = time;
	}
	
	public String getModelID() {
		return this.modelID;
	}
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	
	public String getTime() {
		return this.time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CalloutData{");
        sb.append("modelID'").append(modelID).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

