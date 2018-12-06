package com.bundiconsulting.actuswebapp.models;

import java.util.Map;

//import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.JsonNode;

@Document(collection = "demos")
public class Demo {
    @Id
    String id;
    String contract;
    String identifier;
    String label;
    String description;
    String version;
    Map<String, Object> terms; 
    
    
    // for deserialization 
    public Demo() {
    }

    public Demo(String contract, String identifier, String label, String description, 
    			String version, Map<String, Object>  terms) {
        this.contract = contract;
    	this.identifier = identifier;
        this.label = label;
        this.description = description;
        this.version = version;
        this.terms = terms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContract() {
    	return contract;
    }
    
    public void setContract(String contract) {
    	this.contract = contract;
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLabel() {
    	return label;
    }
    
    public void setLabel(String label) {
    	this.label = label;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public String getVersion() {
    	return version;
    }
    
    public void setVersion(String version) {
    	this.version = version;
    }
    
    public Map<String, Object>  getTerms() {
    	return terms;
    }
    
    public void setTerms(Map<String, Object> terms) {
    	this.terms = terms;
    }
    
}
