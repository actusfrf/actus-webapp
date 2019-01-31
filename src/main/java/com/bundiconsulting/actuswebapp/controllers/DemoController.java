package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Demo;
import com.bundiconsulting.actuswebapp.repositories.DemoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    
    	return demoRepository.findAll();
    }

}
