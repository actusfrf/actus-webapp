package org.actus.webapp.controllers;

import org.actus.webapp.models.DemoMeta;
import org.actus.webapp.repositories.DemoMetaRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoMetaController {

    @Autowired
    DemoMetaRepository demoMetaRepository;

    @RequestMapping(method=RequestMethod.GET, value="/demos/meta")
    @CrossOrigin(origins = "*")
    public Iterable<DemoMeta> getDemoMeta() {
    
        Iterable<DemoMeta> meta = demoMetaRepository.findAll();
        return meta;
    }

    @RequestMapping(method=RequestMethod.GET, value="/demos/meta/{contractType}")
    @CrossOrigin(origins = "*")
    public List<DemoMeta> getDemoMeta(@PathVariable String contractType) {
    
        List<DemoMeta> meta = demoMetaRepository.findByContractType(contractType);
        return meta;
    }

}
