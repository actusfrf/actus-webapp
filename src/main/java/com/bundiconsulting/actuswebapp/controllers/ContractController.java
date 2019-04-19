package com.bundiconsulting.actuswebapp.controllers;

import com.bundiconsulting.actuswebapp.models.Contract;
import com.bundiconsulting.actuswebapp.repositories.ContractRepository;
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
public class ContractController {

    @Autowired
    ContractRepository contractRepository;

    @RequestMapping(method=RequestMethod.GET, value="/contracts")
    @CrossOrigin(origins = "*")
    public Iterable<Contract> contracts() {
        return contractRepository.findAll();
    }

}
