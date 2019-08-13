package org.actus.webapp.controllers;

import org.actus.webapp.models.Event;
import org.actus.webapp.repositories.EventRepository;
import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;
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
        public Set<String> keys() {
            Set<String> keys = new HashSet<String>();
            return keys;
        }

        public double stateAt(String id, LocalDateTime time, StateSpace contractStates,
                ContractModelProvider contractAttributes) {
            return 0.0;
        }
    }

    // String -> ArrayList<ContractEvent>
    @RequestMapping(method = RequestMethod.POST, value = "/events")
    @CrossOrigin(origins = "*")
    public List<Event> solveContract(@RequestBody Map<String, Object> json) {

        // convert json terms object to a java map (required input for actus model parsing)
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : json.entrySet()) {

            System.out.println(entry.getKey() + ":" + entry.getValue());

            //map.put(StringUtils.capitalize(entry.getKey()), entry.getValue().toString());
            // capitalize input json keys as required in contract model parser
            map.put(entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), entry.getValue().toString());
        }

        // parse attributes
        ContractModel model = ContractModel.parse(map);

        // define risk factor (+) observer
        MarketModel observer = new MarketModel();

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

        // transform schedule
        List<Event> output = schedule.stream().map(e -> new Event(e)).collect(Collectors.toList());

        return output;

    }

}