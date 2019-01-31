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

    @Autowired
    DemoRepository demoRepository;


    @RequestMapping(method=RequestMethod.GET, value="/demos/meta/{contractType}")
    public List<Demo> getDemoMeta(@PathVariable String contractType) {
        System.out.println("Hello World!: " + contractType);
    
         List<Demo> meta = demoRepository.findByContractType(contractType);
        return meta;
    }



    @RequestMapping(method=RequestMethod.GET, value="/demos/{id}")
    public Map<String, Object> getDemoTerms(@PathVariable String id) {
    	Optional<Demo> optdemo = demoRepository.findById(id);
        Demo d = optdemo.get();

        return d.getTerms();

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
