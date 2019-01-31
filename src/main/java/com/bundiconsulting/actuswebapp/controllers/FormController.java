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

    @RequestMapping(method=RequestMethod.POST, value="/terms")
    public Form save(@RequestBody Form form) {
        formRepository.save(form);

        return form;
    }

    @RequestMapping(method=RequestMethod.GET, value="/terms/meta/{contractType}")
    public List<Form> getDemoMeta(@PathVariable String contractType) {
        System.out.println("Hello World!: " + contractType);
    
         List<Form> meta = formRepository.findByContractType(contractType);
        return meta;
    }

    @RequestMapping(method=RequestMethod.GET, value="/terms/{id}")
    public Optional<Form> show(@PathVariable String id) {
        return formRepository.findById(id);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/terms/{id}")
    public Form update(@PathVariable String id, @RequestBody Form form) {
        Optional<Form> optform = formRepository.findById(id);
        Form c = optform.get();
       // if(form.getName() != null)
      //      c.setName(form.getName());
    //    if(form.getAddress() != null)
    //        c.setAddress(form.getAddress());
     //   if(form.getCity() != null)
      //      c.setCity(form.getCity());
    //    if(form.getPhone() != null)
     //       c.setPhone(form.getPhone());
    //    if(form.getEmail() != null)
     //       c.setEmail(form.getEmail());
        formRepository.save(c);
        return c;
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/terms/{id}")
    public String delete(@PathVariable String id) {
        Optional<Form> optform = formRepository.findById(id);
        Form form = optform.get();
        formRepository.delete(form);

        return "";
    }
}
