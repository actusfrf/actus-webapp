package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Form;
import com.bundiconsulting.actuswebapp.repositories.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Iterable<Form> form() {
        return formRepository.findAll();
    }

    @RequestMapping(method=RequestMethod.GET, value="/terms/meta/{contractType}")
    public List<Form> getDemoMeta(@PathVariable String contractType) {
   
        List<Form> meta = formRepository.findByContractType(contractType);

        return meta;
    }

    @RequestMapping(method=RequestMethod.GET, value="/terms/{id}")
    public Optional<Form> show(@PathVariable String id) {
       
        return formRepository.findById(id);
        
    }

}
