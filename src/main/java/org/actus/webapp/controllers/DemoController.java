package org.actus.webapp.controllers;

import org.actus.webapp.models.Demo;
import org.actus.webapp.repositories.DemoRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    DemoRepository demoRepository;

    @RequestMapping(method=RequestMethod.GET, value="/demo/{id}")
    @CrossOrigin(origins = "*")
    public Map<String, Object> getDemo(@PathVariable String id) {
        
        Optional<Demo> optdemo = demoRepository.findById(id);
        Demo d = optdemo.get();

        return d.getContract();
    }

    @RequestMapping(method=RequestMethod.GET, value="/demos/{id}")
    @CrossOrigin(origins = "*")
    public List<Demo> getDemos(@PathVariable String id) {
        
        List<Demo> demo = demoRepository.findByContractType(id);

        return demo;
    }
   
    @RequestMapping(method=RequestMethod.GET, value="/demos")
    @CrossOrigin(origins = "*")
    public Iterable<Demo> demo() {
    
    	return demoRepository.findAll();
    }

}
