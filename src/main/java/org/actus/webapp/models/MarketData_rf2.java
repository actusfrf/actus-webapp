package org.actus.webapp.models;

import java.util.List;

public class MarketData_rf2 {
	// attributes
	private List<ReferenceIndex_rf2> marketData;
	
	// null and useful constructors
	public MarketData_rf2() {
    }
	
	public MarketData_rf2(List<ReferenceIndex_rf2> marketData) {
        this.marketData = marketData;
    }
	// get and set for each attribute  
	public List<ReferenceIndex_rf2> getMarketData() {
		return this.marketData;
	}
	public void setMarketData(List<ReferenceIndex_rf2> marketData) {
		this.marketData = marketData;
	}
	// convert to string 
	public String toString() {
		String str = "{\"marketData\": [";
		boolean first = true;
		for (ReferenceIndex_rf2 referenceIndex: this.marketData) {
			if (first) { 
				str += referenceIndex.toString();
				first = false;				
			}
			else 
				str += ", "+referenceIndex.toString();
		}
		str += 	"]}";
		return str;
	}
}
