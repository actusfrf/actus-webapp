package org.actus.webapp.utils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.actus.attributes.ContractModelProvider;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;
import org.actus.webapp.models.StateAtInput;
import org.actus.webapp.models.BehaviorStateAtInput;
import org.springframework.web.client.RestTemplate;
import  org.springframework.beans.factory.annotation.Value;

public class MultiRiskFactorModel_rf2 implements RiskFactorModelProvider{
	
	// properties to configure location of external risk service 
    private 
    String riskserviceHost;
  //  @Value("${actus.riskservice.host}")

    private
    Integer riskservicePort;
  //  @Value("${actus.riskservice.port}")  
		
	
	public MultiRiskFactorModel_rf2() {
	}
	
	public MultiRiskFactorModel_rf2(String riskserviceHost, Integer riskservicePort) {
		this.riskserviceHost = riskserviceHost;
		this.riskservicePort = riskservicePort;
	}

	// this has a warning about type conversion - my not work FNP Aug 2024 
	// but may never get used - we do not want to refine the interface 
	public Set<String> keys() {
		  RestTemplate restTemplate = new RestTemplate();
		  String uri = "http://"+ riskserviceHost+ ':' + riskservicePort + "/marketKeys";
		 // String uri = "http://localhost:8082/marketKeys";	
		  HashSet<String> kset = restTemplate.getForObject(uri,HashSet.class);
		  return kset;
	}

	public double stateAt(String id, LocalDateTime time, StateSpace states, ContractModelProvider terms, boolean isMarket) {
		  // adding code to make this work for both market and behavior observations
		  double dval;
	      if (isMarket) {
	    	  RestTemplate restTemplate = new RestTemplate();
	    	  String uri = "http://"+ riskserviceHost+ ':' + riskservicePort + "/marketStateAt" ;
	    	  // String uri = "http://localhost:8082/marketStateAt";
	    	  StateAtInput stateAtInput = new StateAtInput(id, time);		  
	    	  dval = restTemplate.postForObject(uri, stateAtInput, Double.class );
		  } else {  // observation call out to a behavior rather than a model
	    	  RestTemplate restTemplate = new RestTemplate();
	    	  String uri = "http://"+ riskserviceHost+ ':' + riskservicePort + "/behaviorStateAt";
	    	  // String uri = "http://localhost:8082/behaviorStateAt";
	    	  BehaviorStateAtInput behaviorStateAtInput = new BehaviorStateAtInput(id, states);		  
	    	  dval = restTemplate.postForObject(uri, behaviorStateAtInput, Double.class );
		  }
		  return dval;
	}
	
	

}
