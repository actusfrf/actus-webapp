package org.actus.webapp.controllers;

import org.actus.webapp.models.Event;
import org.actus.webapp.repositories.EventRepository;
import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;
import org.actus.webapp.utils.TimeSeries;
import org.actus.webapp.utils.ActusData;
import org.actus.webapp.utils.ObservedData;
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

    @Autowired
    EventRepository eventRepository;

    class MarketModel implements RiskFactorModelProvider {
        HashMap<String,TimeSeries<LocalDateTime,Double>> multiSeries = new HashMap<String,TimeSeries<LocalDateTime,Double>>();
        
        public Set<String> keys() {
            return multiSeries.keySet();
        }

        public void add(String symbol, TimeSeries<LocalDateTime,Double> series) {
            multiSeries.put(symbol,series);
        }

        public double stateAt(String id, LocalDateTime time, StateSpace contractStates,
                ContractModelProvider contractAttributes) {
            return multiSeries.get(id).getValueFor(time,1);
        }
    }

    // String -> ArrayList<ContractEvent>
    @RequestMapping(method = RequestMethod.POST, value = "/events")
    @CrossOrigin(origins = "*")
    public List<Event> solveContract(@RequestBody Map<String, Object> json) {

        // extract contract terms from body
        ContractModel terms = extractTerms(json);

        // define (empty) risk factor observer
        MarketModel observer = new MarketModel();

        // compute and return events
        return computeEvents(terms, observer);

    }

    // param:   Json Array of Json Objects
    // return:  ArrayList of ArrayList of ContractEvents
    @RequestMapping(method = RequestMethod.POST, value = "/eventsBatch")
    @CrossOrigin(origins = "*")
    public List<List<Event>> solveArrayOfContract(@RequestBody ActusData json) {
        
        // extract body parameters
        List<Map<String, Object>> contractData = json.getContracts();
        List<ObservedData> riskFactorData = json.getRiskFactors();

        // create risk factor observer
        RiskFactorModelProvider observer = createObserver(riskFactorData);

        ArrayList<List<Event>> output = new ArrayList<>();
        contractData.forEach(entry -> {
            //minimal error handling, can be removed as soon as error handling in solveContract is implemented
            try {
                ContractModel terms = extractTerms(entry);
                output.add(computeEvents(terms, observer));
            }catch(Exception e){
                System.err.println("Invalid Contract Parameters\nContractID: " + entry.get("ContractID"));
            }
        });
        return output;
    }

    private ContractModel extractTerms(Map<String,Object> json) {
        // convert json terms object to a java map (required input for actus model parsing)
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : json.entrySet()) {

            System.out.println(entry.getKey() + ":" + entry.getValue());

            // capitalize input json keys as required in contract model parser
            map.put(entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), entry.getValue().toString());
        }

        // parse attributes
        return ContractModel.parse(map);   
    }

    private RiskFactorModelProvider createObserver(List<ObservedData> json) {
        MarketModel observer = new MarketModel();

        json.forEach(entry -> {
            String symbol = entry.getMarketObjectCode();
            Double base = entry.getBase();
            LocalDateTime[] times = entry.getData().stream().map(obs -> LocalDateTime.parse(obs.getTime())).toArray(LocalDateTime[]::new);
            Double[] values = entry.getData().stream().map(obs -> obs.getValue()).toArray(Double[]::new);
            
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