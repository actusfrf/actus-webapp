package com.bundiconsulting.actuswebapp.models;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.JsonNode;


public class Terms {
    String contractType;
    String calendar;
    String statusDate;
    String contractRole;
    String legalEntityIDCounterparty;
    String dayCountConvention;
    String currency;
    String initialExchangeDate;
    String maturityDate;
    String notionalPrincipal;
    
    
    // for deserialization 
    public Terms() {
    }

    public Terms(String contractType, String calendar, String statusDate, String contractRole, 
    			String legalEntityIDCounterparty, String dayCountConvention, String currency,
    			String initialExchangeDate, String maturityDate, String notionalPrincipal) {
        
    	this.contractType = contractType;
    	this.calendar = calendar;
        this.statusDate = statusDate;
        this.contractRole = contractRole;
        this.legalEntityIDCounterparty = legalEntityIDCounterparty;
        this.dayCountConvention = dayCountConvention;
        this.currency = currency;
        this.initialExchangeDate = initialExchangeDate;
        this.maturityDate = maturityDate;
        this.notionalPrincipal = notionalPrincipal;
        
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
    
    public String getCalendar(){
    	return calendar;
    }
    
    public void setCalendar(String calendar) {
    	this.calendar = calendar;
    }
    
    public String getStatusDate(){
    	return statusDate;
    }
    
    public void setStatusDate(String statusDate) {
    	this.statusDate = statusDate;
    }
    
    public String getContractRole(){
    	return contractRole;
    }
    
    public void setcontractRole(String contractRole) {
    	this.contractRole = contractRole;
    }
    
    public String getLegalEntityIDCounterparty(){
    	return legalEntityIDCounterparty;
    }
    
    public void setlegalEntityIDCounterparty(String legalEntityIDCounterparty) {
    	this.legalEntityIDCounterparty = legalEntityIDCounterparty;
    }
    
    public String getDayCountConvention(){
    	return dayCountConvention;
    }
    
    public void setdayCountConvention(String dayCountConvention) {
    	this.dayCountConvention = dayCountConvention;
    }
    
    public String getCurrency(){
    	return currency;
    }
    
    public void setCurrency(String currency) {
    	this.currency = currency;
    }
    
    public String getInitialExchangeDate(){
    	return initialExchangeDate;
    }
    
    public void setinitialExchangeDate(String initialExchangeDate) {
    	this.initialExchangeDate = initialExchangeDate;
    }
    
    public String getmaturityDate(){
    	return maturityDate;
    }
    
    public void setMaturityDate(String maturityDate) {
    	this.maturityDate = maturityDate;
    }
    
    public String getNotionalPrincipal(){
    	return notionalPrincipal;
    }
    
    public void setNotionalPrincipal(String notionalPrincipal) {
    	this.notionalPrincipal = notionalPrincipal;
    }

    
}