package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Form;
import com.bundiconsulting.actuswebapp.repositories.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
public class FormController {

    @Autowired
    FormRepository formRepository;


    @RequestMapping(method=RequestMethod.GET, value="/terms")
    @CrossOrigin(origins = "*")
    public Iterable<Form> form() {
        return formRepository.findAll();
    }


    @RequestMapping(method=RequestMethod.GET, value="/terms/meta/{contractType}")
    @CrossOrigin(origins = "*")
    public List<Form> getDemoMeta(@PathVariable String contractType) {
   
        List<Form> meta = formRepository.findByContractType(contractType);

        return meta;
    }

    @RequestMapping(method=RequestMethod.GET, value="/terms/{id}")
    @CrossOrigin(origins = "*")
    public Optional<Form> show(@PathVariable String id) {
       
        return formRepository.findById(id);
        
    }

}
