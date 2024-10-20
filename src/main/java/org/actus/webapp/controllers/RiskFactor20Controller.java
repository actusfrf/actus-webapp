package org.actus.webapp.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.events.EventFactory;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.pam.POF_AD_PAM;
import org.actus.functions.pam.STF_AD_PAM;
import org.actus.states.StateSpace;
import org.actus.types.EventType;
import org.actus.webapp.core.functions.POF_PP_rf2;
import org.actus.webapp.core.functions.STF_PP_rf2;
import org.actus.webapp.models.BatchInputData_rf2;
import org.actus.webapp.models.BatchStartInput;
import org.actus.webapp.models.CalloutData;
import org.actus.webapp.models.ContractUserData;
import org.actus.webapp.models.Event;
import org.actus.webapp.models.EventStream;
import org.actus.webapp.models.EventStream2;
import org.actus.webapp.models.MarketData_rf2;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.ReferenceIndex_rf2;
import org.actus.webapp.models.ScenarioSimulationInput_rf2;
import org.actus.webapp.models.ScenarioDescriptor;
import org.actus.webapp.utils.MultiRiskFactorModel_rf2;
import org.actus.webapp.utils.TimeSeries;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import  org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;



@RestController
public class RiskFactor20Controller {
	
    class MarketModel implements RiskFactorModelProvider {
        HashMap<String,TimeSeries<LocalDateTime,Double>> multiSeries = new HashMap<String,TimeSeries<LocalDateTime,Double>>();
        
        public Set<String> keys() {
            return multiSeries.keySet();
        }

        public void add(String symbol, TimeSeries<LocalDateTime,Double> series) {
            multiSeries.put(symbol,series);
        }

        public double stateAt(String id, LocalDateTime time, StateSpace states,
                ContractModelProvider terms, boolean isMarket) {
            return multiSeries.get(id).getValueFor(time,1);
        }
    }	
    
	// properties to configure location of external risk service 
    private
    @Value("${actus.riskservice.host}")
    String riskserviceHost;

    private
    @Value("${actus.riskservice.port}")
    Integer riskservicePort;

	  @RequestMapping(method = RequestMethod.POST, value = "/rf2/eventsBatch")
	  @CrossOrigin(origins = "*")
      public List<EventStream> solveContractBatch_rf2(@RequestBody BatchInputData_rf2 json) {
	        
		  // extract body parameters
	      List<Map<String, Object>> contractData = json.getContracts();
	      String           scenarioID   = json.getScenarioDescriptor().getScenarioID();
	        
	      // fetch Market data for scn01 - ignoring input 
	      System.out.println("**** rf2EventsBatch/doGetMarketData - request to risksrv3.");
	      String uri = "http://"+ riskserviceHost+ ':' + riskservicePort + "/marketData/" + scenarioID ;
	      System.out.println("**** uri = <"+ uri +">");
	      RestTemplate restTemplate = new RestTemplate();
	      MarketData_rf2 marketData = restTemplate.getForObject(uri, MarketData_rf2.class );
	        
	      List<ObservedData> riskFactorData = marketData2RiskFactors(marketData);
	        
	      //      List<ObservedData> riskFactorData = json.getRiskFactors();

	      // create risk factor observer
	      RiskFactorModelProvider observer = createObserver(riskFactorData);

	      ArrayList<EventStream> output = new ArrayList<>();
	      contractData.forEach(entry -> {
	      // extract contract terms
	            ContractModel terms;
	            String contractID = (entry.get("contractID") == null)? "NA":entry.get("contractID").toString();
	            try {
	                terms = ContractModel.parse(entry); 
	            } catch(Exception e){
	                output.add(new EventStream(contractID, "Failure", e.toString(), new ArrayList<Event>()));
	                return; // skip this iteration and continue with next
	            }
	            // compute contract events
	            try {
	                output.add(new EventStream(contractID, "Success", "", computeEvents(terms, observer)));
	            }catch(Exception e){
	                output.add(new EventStream(contractID, "Failure", e.toString(), new ArrayList<Event>()));
	            }
	        });
	        return output;
	    }
	    private RiskFactorModelProvider createObserver(List<ObservedData> json) {
	        MarketModel observer = new MarketModel();

	        json.forEach(entry -> {
	            String symbol = entry.getMarketObjectCode();
	            Double base = entry.getBase();
	            LocalDateTime[] times = entry.getData().stream().map(obs -> LocalDateTime.parse(obs.getTime())).toArray(LocalDateTime[]::new);
	            Double[] values = entry.getData().stream().map(obs -> 1/base*obs.getValue()).toArray(Double[]::new);
	            
	            TimeSeries<LocalDateTime,Double> series = new TimeSeries<LocalDateTime,Double>();
	            series.of(times,values);
	            observer.add(symbol,series);
	        });
	        return observer;
	    }

	    private List<Event> computeEvents(ContractModel model, RiskFactorModelProvider observer) {
	        // define projection end-time
	        LocalDateTime to = model.getAs("TerminationDate");
	        if(to == null) to = model.getAs("MaturityDate");
	        if(to == null) to = model.getAs("AmortizationDate");
	        if(to == null) to = model.getAs("SettlementDate");
	        if(to == null) to = LocalDateTime.now().plusYears(5);

	        // compute actus schedule
	        ArrayList<ContractEvent> schedule = ContractType.schedule(to, model);

	        // apply schedule to contract
	        schedule = ContractType.apply(schedule, model, observer);
	        
	        // transform schedule to event list and return
	        return schedule.stream().map(e -> new Event(e)).collect(Collectors.toList());
	    }
	  
	    List<ObservedData> marketData2RiskFactors(MarketData_rf2 marketData ){ 
	    	List<ReferenceIndex_rf2> rfxs = marketData.getMarketData();
	    	List<ObservedData> riskFactors =  new ArrayList<ObservedData> ( ) ;
	    	for ( ReferenceIndex_rf2  rfx :  rfxs  ){
			  riskFactors.add(new ObservedData( rfx.getMarketObjectCode(), rfx.getBase(), rfx.getData()) );			  
	    	}
	    	return riskFactors;
	    }
	  
	    @RequestMapping(method = RequestMethod.POST, value = "/rf2/scenarioSimulation")
	    @ResponseBody
	    @CrossOrigin(origins = "*")
	    public List<EventStream2> runScenarioSimulation(@RequestBody ScenarioSimulationInput_rf2 json) {
	        
	        System.out.println("****fnp001 Started a scenario simulation");  // fnp diagnostic aug 2024  
	        // extract body parameters
	        ScenarioDescriptor scenarioDescriptor = json.getScenarioDescriptor();
	        String scenarioId = scenarioDescriptor.getScenarioID();
	        List<Map<String, Object>> contractData = json.getContracts();
	        LocalDateTime simulateTo = json.getSimulateTo();
	        Set<LocalDateTime> monitoringTimes = json.getMonitoringTimes();
	        
	        // Step1:  REST invocation to external RiskService a PostforObject call 
	        System.out.println("****fnp002  Call /Started a scenario simulation");  // fnp diagnostic aug 2024 
	        // call risksrv3:/scenarioSimulationStart passing  the scenarioDescriptor from request data 
	        String uri = "http://"+ riskserviceHost+ ':' + riskservicePort + "/scenarioSimulationStart" ;
			// String uri = "http://localhost:8082/scenarioSimulationStart";
			RestTemplate restTemplate = new RestTemplate();	  
			String outstr = restTemplate.postForObject(uri, scenarioDescriptor, String.class );
		    System.out.println("****fnp003 return /scenarioSimulationStart " + outstr);  // fnp diagnostic aug 2024 
		    
	        // fetch scenario data and create risk factor observer
		    // BUT no need to do this  with external risk - all lookup is remote
		    // just create a multiRiskFactorModel 
		    
		    RiskFactorModelProvider observer; 
		    observer = createObserver();
		    

	        // for each contract compute events
	        
		    ArrayList<EventStream2> output = new ArrayList<>();
	        contractData.forEach(entry -> {
	        // extract contract terms - this is parsing the contractTerms and necessary BUT no auxiliary  
	        ContractModel terms;
	        String contractID = (entry.get("contractID") == null)? "NA":entry.get("contractID").toString();
	        Map<String,Object> attributes = entry;
	        try {  	        	   
	               terms = ContractModel.parse(entry);
	            // if(entry.get("auxiliaryTerms")!=null) {
	            //        ((List<HashMap<String,String>>) entry.get("auxiliaryTerms")).forEach(term -> terms.addAttribute(term.get("id"),term.get("value")));
	            //    }
	               
	            // If a contract did not parse skip this one and continue with next  
	            } catch(Exception e){
	               output.add(new EventStream2(scenarioId, contractID, "Failure", e.toString(), new ArrayList<Event>()));
	               return; // skip this iteration and continue with next
	            }
	            
	        // *** both adding behavior model events AND simulation is in this stanza - compute events  
	        // compute contract events
	        //computeEvents  should NOT need a scenario - just use the observer for all scenario related info 
	        try {
	                output.add(new EventStream2(scenarioId, contractID, "Success", "", computeEvents(terms, observer, simulateTo, 
	                																				 monitoringTimes, attributes)));
	            }catch(Exception e){
	                output.add(new EventStream2(scenarioId, contractID, "Failure", e.toString(), new ArrayList<Event>()));
	            }
	        });
	        return output;
	    }

	    private RiskFactorModelProvider createObserver() {
	        MultiRiskFactorModel_rf2 observer = new MultiRiskFactorModel_rf2(riskserviceHost,riskservicePort);     
	        return observer;
	    }

	    // scenario is removed from compute events - all logic to add events commented out 
	    private List<Event> computeEvents(ContractModel model, RiskFactorModelProvider observer, LocalDateTime to, 
	                                    Set<LocalDateTime> monitoringTimes, Map<String,Object> attributes) {

	        // define simulation horizon if not provided
	        if(to == null) to = model.getAs("MaturityDate");
	        if(to == null) to = LocalDateTime.now().plusYears(5);

	        // compute actus schedule
	        ArrayList<ContractEvent> schedule = ContractType.schedule(to, model);

	        // add monitoring events if defined
	        // ok because monitoring times in simulation call not risk 
	        if(monitoringTimes != null && monitoringTimes.size()>0) {
	            schedule.addAll(EventFactory.createEvents(
	                        monitoringTimes,
	                        EventType.AD,
	                        model.getAs("Currency"),
	                        new POF_AD_PAM(),
	                        new STF_AD_PAM(),
	                        model.getAs("BusinessDayConvention"),
	                        model.getAs("ContractID")
	                ));
	        }

	        // call out to risk service /scenarioSimulationContractStart it will decide whether
	        // any  prepayment behavior model is activated and return populated or empty List<CallOutData> 
	        RestTemplate restTemplate = new RestTemplate(); 
	        String uri = "http://"+ riskserviceHost+ ':' + riskservicePort + "/contractSimulationStart" ;
	    	// String uri = "http://localhost:8082/contractSimulationStart";
	    	System.out.println("****fnp100  in ppcallouts about to post ContractStart request ") ;  // fnp diagnostic aug 2024 
	    	List<Object> items = restTemplate.postForObject(uri, attributes, List.class);
	    	System.out.println("****fnp101  returned  items = " + items.toString() + "items.size=  " + items.size()) ;
	    	List<CalloutData> ppcallouts  = new ArrayList<CalloutData>();
	    	for (Object item : items) {
	    		System.out.println("****fnp102  returned  item= " + item.toString() + "item.class = " + item.getClass().toString()) ;
	    		HashMap<String,String> fromItem = (HashMap<String,String>) item;
	    		CalloutData calloutData = new CalloutData(fromItem.get("modelID"),fromItem.get("time"));
	    		ppcallouts.add(calloutData);
	    	}
	    	System.out.println("****fnp103  ppcallouts= " + ppcallouts.toString()) ;
	    	
	        // add any returned prepayment observations into the event schedule 
	    	ArrayList<ContractEvent> prepaymentEvents = new ArrayList<ContractEvent>();
	    	for (CalloutData calloutData : ppcallouts ) {
	    		// set up a behavior observation / call out event for this model at this time 
	    	    prepaymentEvents.add(EventFactory.createEvent(
	    	    		LocalDateTime.parse(calloutData.getTime()),
	    	    		EventType.PP,
	    	    		model.getAs("currency"),
	    	    		new POF_PP_rf2(calloutData.getModelID()),
	    	    		new STF_PP_rf2(calloutData.getModelID()),
	    	    		model.getAs("BusinessDayConvention"),
	    	  	        model.getAs("ContractID")
	    	    		));    			
	    	}
	    	schedule.addAll(prepaymentEvents);
	        // apply schedule to contract
	        schedule = ContractType.apply(schedule, model, observer);

	        System.out.println("****fnp008 ContractType.apply passed");       // fnp diagnostic jan 2023      
	        // transform schedule to event list and return
	        return schedule.stream().map(e -> new Event(e)).collect(Collectors.toList());
	    }


	        
	        // add prepayment events if prepayment model referenced by contract
	        // if(!CommonUtils.isNull(model.getAs("ObjectCodeOfPrepaymentModel"))) {
	        //     String modelId = model.getAs("ObjectCodeOfPrepaymentModel");
	        //     TwoDimensionalPrepaymentModelData modelData = scenario.getTwoDimensionalPrepaymentModelData().
	        //             stream().filter(dat -> dat.getRiskFactorId().equals(modelId)).collect(Collectors.toList()).get(0);
	        //     List<String> prepaymentEventTimes = modelData.getPrepaymentEventTimes();
	        //    IntStream stream = IntStream.range(0,prepaymentEventTimes.size()); // start inclusive, end exclusive
	        //    ArrayList<ContractEvent> prepaymentEvents = new ArrayList<ContractEvent>();
	        //    stream.forEach(i -> prepaymentEvents.add(EventFactory.createEvent(
	        //        LocalDateTime.parse(prepaymentEventTimes.get(i)),
	        //        EventType.PP,
	        //        model.getAs("Currency"),
	        //        new POF_PP(),
	        //        new STF_PP(),
	        //        model.getAs("BusinessDayConvention"),
	        //        model.getAs("ContractID")
	        //    )));
	        //    schedule.addAll(prepaymentEvents);
	        //    }

	        // comment out stanza to add deposit withdrawal events 
	        // add deposit withdrawal events if withdrawal model referenced by UMP contract
	        // if(model.getAs("ContractType").equals(ContractTypeEnum.UMP) && !CommonUtils.isNull(model.getAs("ObjectCodeOfCashBalanceModel"))) {
		    //    String modelId = model.getAs("ObjectCodeOfCashBalanceModel");

	        //    TwoDimensionalDepositTrxModelData modelData = scenario.getTwoDimensionalDepositTrxModelData().
	        //            stream().filter(dat -> dat.getRiskFactorId().equals(modelId)).collect(Collectors.toList()).get(0);
	        //    List<String> transactionEventTimes = modelData.getDepositTrxEventTimes();
	        //    IntStream stream = IntStream.range(0,transactionEventTimes.size()); // start inclusive, end exclusive
	        //    ArrayList<ContractEvent> transactionEvents = new ArrayList<ContractEvent>();
	        //    stream.forEach(i -> transactionEvents.add(EventFactory.createEvent(
	        //        LocalDateTime.parse(transactionEventTimes.get(i)),
	        //        EventType.PR,
	        //        model.getAs("Currency"),
	        //        new POF_CW(),
	        //        new STF_CW(),
	        //        model.getAs("BusinessDayConvention"),
	        //        model.getAs("ContractID")
	        //    )));
	        //    schedule.addAll(transactionEvents);
	        // }

	        // credit loss event addition stanza 
	        // add credit loss events if credit loss model referenced by contract
	        // if(!CommonUtils.isNull(model.getAs("ObjectCodeOfCreditLossModel"))) {
	        //     String modelId = model.getAs("ObjectCodeOfCreditLossModel");
	        //     TwoDimensionalCreditLossModelData modelData = scenario.getTwoDimensionalCreditLossModelData().
	        //             stream().filter(dat -> dat.getRiskFactorId().equals(modelId)).collect(Collectors.toList()).get(0);
	        //     List<String> creditEventTimes = modelData.getCreditEventTimes();
	        //     List<Double> creditEventWeights = modelData.getCreditEventWeights();
	        //     IntStream stream = IntStream.range(0,creditEventTimes.size()); // start inclusive, end exclusive
	        //     ArrayList<ContractEvent> creditEvents = new ArrayList<ContractEvent>();
	        //     stream.forEach(i -> creditEvents.add(EventFactory.createEvent(
	        //         LocalDateTime.parse(creditEventTimes.get(i)),
	        //         EventType.CE,
	        //         model.getAs("Currency"),
	        //         new POF_CL(creditEventWeights.get(i)),
	        //         new STF_CL(creditEventWeights.get(i)),
	        //         model.getAs("BusinessDayConvention"),
	        //         model.getAs("ContractID")
	        //    )));
	        //    schedule.addAll(creditEvents);
	        // }	    
}
