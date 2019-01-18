package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Event;
import com.bundiconsulting.actuswebapp.repositories.EventRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.contracts.PrincipalAtMaturity;
import org.actus.events.ContractEvent;


import org.actus.externals.RiskFactorModelProvider;
import org.actus.states.StateSpace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

//    @RequestMapping(method=RequestMethod.POST, value="/events")
//    public Iterable<Event> form() {
//
//
//        return eventRepository.findAll();
//    }
//
//    @RequestMapping(method=RequestMethod.POST, value="/events")
//    public Event save(@RequestBody Event form) {
//        eventRepository.save(form);
//
//        return form;
//    }
class MarketModel implements RiskFactorModelProvider {
    public Set<String> keys() {
        Set<String> keys = new HashSet<String>();
        return keys;
    }

    public double stateAt(String id,LocalDateTime time,StateSpace contractStates,ContractModelProvider contractAttributes) {
        return 0.0;
    }
}

    // String -> ArrayList<ContractEvent>
    @RequestMapping(method=RequestMethod.POST, value="/events")
    public List<Event> solveContract(@RequestBody Map<String, Object> json ) {

        Map<String,String> map = new HashMap<String,String>();


        for (Map.Entry<String, Object> entry : json.entrySet()) {
         System.out.println(entry.getKey() + ":" + entry.getValue());
           map.put(entry.getKey(),  entry.getValue().toString());

        }

     //   ContractModelProvider modelProvider = ContractModelProvider.parse( json );

     //  ArrayList<ContractEvent> events = ContractType.lifecycle(times,model,riskfactors);




     //   TypeFactory factory = TypeFactory.defaultInstance();
     //   MapType type = factory.constructMapType(HashMap.class, String.class, String.class);
        ObjectMapper mapper  = new ObjectMapper();


//        try {
//            map = mapper.readValue(terms, type);
//        } catch (JsonParseException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (JsonMappingException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

        // parse attributes
        ContractModel model = ContractModel.parse(map);

        // define analysis times
       Set<LocalDateTime> analysisTimes = new HashSet<LocalDateTime>();
        //analysisTimes.add(LocalDateTime.parse("2019-01-01T00:00:00")); // needs to be synced with dates in terms objects
        analysisTimes.add(LocalDateTime.parse("2015-01-01T00:00:00"));
        
        // define risk factor model
          MarketModel riskFactors = new MarketModel();


        // lifecycle PAM contract
        ArrayList<ContractEvent> events = PrincipalAtMaturity.lifecycle(analysisTimes,model,riskFactors);




        List<Event> output  = events.stream().map(e -> new Event(e)).collect(Collectors.toList());

        return output;
    }


}
