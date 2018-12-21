package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Demo;
import com.bundiconsulting.actuswebapp.models.Event;
import com.bundiconsulting.actuswebapp.repositories.DemoRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
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


@RestController
public class DemoController {

	class MarketModel implements RiskFactorModelProvider {
        public Set<String> keys() {
            Set<String> keys = new HashSet<String>();
            return keys;
        }

        public double stateAt(String id,LocalDateTime time,StateSpace contractStates,ContractModelProvider contractAttributes) {
            return 0.0;
        }
    }

    @Autowired
    DemoRepository demoRepository;


    @RequestMapping(method=RequestMethod.GET, value="/demos/meta/{contractType}")
    public List<Demo> getDemoMeta(@PathVariable String contractType) {
        System.out.println("Hello World!: " + contractType);
    	//List<Demo> meta = StreamSupport.stream(demoRepository.findAll().spliterator(), false).filter(x -> ct.equals(x.getContract())).collect(Collectors.toList());
         List<Demo> meta = demoRepository.findByContractType(contractType);
        return meta;
    }




    @RequestMapping(method=RequestMethod.GET, value="/demos/{id}")
    public Map<String, Object> getDemoTerms(@PathVariable String id) {
    	Optional<Demo> optdemo = demoRepository.findById(id);
        Demo d = optdemo.get();

        return d.getTerms();

    }

    // String -> ArrayList<ContractEvent>
    @RequestMapping(method=RequestMethod.POST, value="/events")
    public List<Event> solveContract(@RequestBody
    		//String contractType, String calendar, String statusDate, String contractRole,
			//String legalEntityIDCounterparty, String dayCountConvention, String currency,
			//String initialExchangeDate, String maturityDate, String notionalPrincipal) {
    		String terms) {

    	// define attributes
        //Map<String, String> map = new HashMap<String, String>();

        //map.put("ContractType", contractType);
        //map.put("Calendar", calendar);
        //map.put("StatusDate", statusDate);
        //map.put("ContractRole", contractRole);
        //map.put("LegalEntityIDCounterparty", legalEntityIDCounterparty);
        //map.put("DayCountConvention", dayCountConvention);
        //map.put("Currency", currency);
        //map.put("InitialExchangeDate", initialExchangeDate);
        //map.put("MaturityDate", maturityDate);
        //map.put("NotionalPrincipal", notionalPrincipal);

        /*map.put("ContractType", "PAM");
        map.put("Calendar", "NoHolidayCalendar");
        map.put("StatusDate", "2016-01-01T00:00:00");
        map.put("ContractRole", "RPA");
        map.put("LegalEntityIDCounterparty", "CORP-XY");
        map.put("DayCountConvention", "A/AISDA");
        map.put("Currency", "USD");
        map.put("InitialExchangeDate", "2016-01-02T00:00:00");
        map.put("MaturityDate", "2017-01-01T00:00:00");
        map.put("NotionalPrincipal", "1000.0");*/

    	//Map<String,Object> map = new ObjectMapper().readValue(terms, HashMap.class);

    	TypeFactory factory = TypeFactory.defaultInstance();
    	MapType type = factory.constructMapType(HashMap.class, String.class, String.class);
    	ObjectMapper mapper  = new ObjectMapper();
    	Map<String, String> map = null;

		try {
			map = mapper.readValue(terms, type);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // parse attributes
        ContractModel model = ContractModel.parse(map);
        // define analysis times
        Set<LocalDateTime> analysisTimes = new HashSet<LocalDateTime>();
        analysisTimes.add(LocalDateTime.parse("2016-01-01T00:00:00"));
        // define risk factor model
        MarketModel riskFactors = new MarketModel();
        // lifecycle PAM contract
        ArrayList<ContractEvent> events = PrincipalAtMaturity.lifecycle(analysisTimes,model,riskFactors);

        List<Event> output = events.stream().map(e -> new Event(e)).collect(Collectors.toList());

        return output;
    }

    @RequestMapping(method=RequestMethod.GET, value="/demos")
    public Iterable<Demo> demo() {
    	System.out.println("Hello World!");
    	return demoRepository.findAll();
    }


    @RequestMapping(method=RequestMethod.PUT, value="/demos/{id}")
    public Demo update(@PathVariable String id, @RequestBody Demo demo) {
        Optional<Demo> optdemo = demoRepository.findById(id);
        Demo d = optdemo.get();
     //   if(demo.getContract() != null)
       // 	d.setContract(demo.getContract());
        if(demo.getIdentifier() != null)
        	d.setIdentifier(demo.getIdentifier());
        if(demo.getLabel() != null)
        	d.setLabel(demo.getLabel());
        if(demo.getDescription() != null)
        	d.setDescription(demo.getDescription());
        if(demo.getVersion() != null)
        	d.setVersion(demo.getVersion());
        if(demo.getTerms() != null)
        //	d.setTerms(demo.getTerms());
        demoRepository.save(d);
        return demo;

    }

    @RequestMapping(method=RequestMethod.POST, value="/demos")
    public Demo save(@RequestBody Demo demo) {
        demoRepository.save(demo);

        return demo;
    }
}
