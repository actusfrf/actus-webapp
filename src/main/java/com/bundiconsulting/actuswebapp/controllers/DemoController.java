package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Contact;
import com.bundiconsulting.actuswebapp.models.Demo;
import com.bundiconsulting.actuswebapp.models.Event;
import com.bundiconsulting.actuswebapp.repositories.DemoRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
    
    
    @RequestMapping(method=RequestMethod.GET, value="/demos/meta/{ct}")
    public List<Demo> getDemoMeta(@PathVariable String ct) {
    	List<Demo> meta = StreamSupport.stream(demoRepository.findAll().spliterator(), false).filter(x -> ct.equals(x.getContract())).collect(Collectors.toList());

    	return meta;
    }
    
    /*@RequestMapping(method=RequestMethod.GET, value="/demos/terms/{id}")
    public Map<String, Object> getDemoTerms(@PathVariable String id) {
    	Optional<Demo> terms = demoRepository.findById(id);
    	
    	return terms.getTerms();
    }*/
    
    @RequestMapping(method=RequestMethod.GET, value="/demos/terms/{id}")
    public Map<String, Object> getDemoTerms(@PathVariable String id) {
    	Optional<Demo> optdemo = demoRepository.findById(id);
        Demo d = optdemo.get();
        
        return d.getTerms();
        
    }
    
    // String -> ArrayList<ContractEvent>
    @RequestMapping(method=RequestMethod.POST, value="/events")
    public List<Event> solveContract(@RequestBody  
    		String contractType, String calendar, String statusDate, String contractRole, 
			String legalEntityIDCounterparty, String dayCountConvention, String currency,
			String initialExchangeDate, String maturityDate, String notionalPrincipal) {

        // define attributes
        Map<String, String> map = new HashMap<String, String>();
        /*map.put("ContractType", contractType);
        map.put("Calendar", calendar);
        map.put("StatusDate", statusDate);
        map.put("ContractRole", contractRole);
        map.put("LegalEntityIDCounterparty", legalEntityIDCounterparty);
        map.put("DayCountConvention", dayCountConvention);
        map.put("Currency", currency);
        map.put("InitialExchangeDate", initialExchangeDate);
        map.put("MaturityDate", maturityDate);
        map.put("NotionalPrincipal", notionalPrincipal);*/
        map.put("ContractType", "PAM");
        map.put("Calendar", "NoHolidayCalendar");
        map.put("StatusDate", "2016-01-01T00:00:00");
        map.put("ContractRole", "RPA");
        map.put("LegalEntityIDCounterparty", "CORP-XY");
        map.put("DayCountConvention", "A/AISDA");
        map.put("Currency", "USD");
        map.put("InitialExchangeDate", "2016-01-02T00:00:00");
        map.put("MaturityDate", "2017-01-01T00:00:00");
        map.put("NotionalPrincipal", "1000.0");
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
        
        return output; // --> ArrayList zu JSON konvertieren?
        //return "Test";
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
        if(demo.getContract() != null)
        	d.setContract(demo.getContract());
        if(demo.getIdentifier() != null)
        	d.setIdentifier(demo.getIdentifier());
        if(demo.getLabel() != null)
        	d.setLabel(demo.getLabel());
        if(demo.getDescription() != null)
        	d.setDescription(demo.getDescription());
        if(demo.getVersion() != null)
        	d.setVersion(demo.getVersion());
        if(demo.getTerms() != null)
        	d.setTerms(demo.getTerms());
        demoRepository.save(d);
        return demo;
        
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/demos")
    public Demo save(@RequestBody Demo demo) {
        demoRepository.save(demo);

        return demo;
    }
    
    
    /*@RequestMapping(method=RequestMethod.GET, value="/demos/meta/{ct}")
    public Iterable<Demo> demoContracts() {
    	return demoRepository.findByContract();
    }*/
    
    
    
    
    /*@RequestMapping(method=RequestMethod.PUT, value="/demos/{id}")
    public Demo update(@PathVariable String id, @RequestBody Demo demo) {
        Demo d = demoRepository.findOne(id);
        d.setIdentifier("Test");
        d.setLabel("asdf");
        d.setDescription("Beschreibung");;
        d.setVersion(2);
        d.setTerms("{“ContractID”:”pam_demo_1”,“ContractDealDate”:”2018-01-01T00:00:00”,“InitialExchangeDate”:”2018-01-02T00:00:00”,“MaturityDate”:”2018-12-31T23:59:59”,“NotionalPrincipal”:1000.0,“NominalInterestRate”:0.01}");
        demoRepository.save(d);
        return demo;
    }*/
    
    /*
    @RequestMapping(method=RequestMethod.GET, value="/contacts")
    public Iterable<Contact> contact() {
        return contactRepository.findAll();
    }

    @RequestMapping(method=RequestMethod.POST, value="/contacts")
    public Contact save(@RequestBody Contact contact) {
        contactRepository.save(contact);

        return contact;
    }

    @RequestMapping(method=RequestMethod.GET, value="/contacts/{id}")
    public Contact show(@PathVariable String id) {
        return contactRepository.findOne(id);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/contacts/{id}")
    public Contact update(@PathVariable String id, @RequestBody Contact contact) {
        Contact c = contactRepository.findOne(id);
        if(contact.getName() != null)
            c.setName(contact.getName());
        if(contact.getAddress() != null)
            c.setAddress(contact.getAddress());
        if(contact.getCity() != null)
            c.setCity(contact.getCity());
        if(contact.getPhone() != null)
            c.setPhone(contact.getPhone());
        if(contact.getEmail() != null)
            c.setEmail(contact.getEmail());
        contactRepository.save(c);
        return contact;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/contacts/{id}")
    public String delete(@PathVariable String id) {
        Contact contact = contactRepository.findOne(id);
        contactRepository.delete(contact);

        return "";
    }*/
}
