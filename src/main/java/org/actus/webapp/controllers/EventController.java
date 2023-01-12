package org.actus.webapp.controllers;

import org.actus.webapp.models.Event;
import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;
import org.actus.webapp.utils.TimeSeries;
import org.actus.webapp.models.InputData;
import org.actus.webapp.models.BatchInputData;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.EventStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class EventController {

    class MarketModel implements RiskFactorModelProvider {
        HashMap<String,TimeSeries<LocalDateTime,Double>> multiSeries = new HashMap<String,TimeSeries<LocalDateTime,Double>>();
        
        public Set<String> keys() {
            return multiSeries.keySet();
        }

        public void add(String symbol, TimeSeries<LocalDateTime,Double> series) {
            multiSeries.put(symbol,series);
        }

        public double stateAt(String id, LocalDateTime time, StateSpace states,
                ContractModelProvider terms) {
            return multiSeries.get(id).getValueFor(time,1);
        }
    }

    // String -> ArrayList<ContractEvent>
    @RequestMapping(method = RequestMethod.POST, value = "/events")
    @CrossOrigin(origins = "*")
    public List<Event> solveContract(@RequestBody InputData json) {

        // extract contract terms from body
        ContractModel terms = ContractModel.parse(json.getContract());
        List<ObservedData> riskFactorData = json.getRiskFactors();

        // create risk factor observer
        RiskFactorModelProvider observer = createObserver(riskFactorData);

        // compute and return events
        return computeEvents(terms, observer);

    }

    // param:   Json Array of Json Objects
    // return:  ArrayList of ArrayList of ContractEvents
    @RequestMapping(method = RequestMethod.POST, value = "/eventsBatch")
    @CrossOrigin(origins = "*")
    public List<EventStream> solveContractBatch(@RequestBody BatchInputData json) {
        
        // extract body parameters
        List<Map<String, Object>> contractData = json.getContracts();
        List<ObservedData> riskFactorData = json.getRiskFactors();

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
                return; // skipt this iteration and continue with next
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

}