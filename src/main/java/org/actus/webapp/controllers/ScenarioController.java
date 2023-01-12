package org.actus.webapp.controllers;

import org.actus.webapp.models.ScenarioIdInput;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.ScenarioData;
import org.actus.webapp.repositories.ScenarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScenarioController {

    @Autowired
    ScenarioRepository scenarioRepository;

    @RequestMapping(method=RequestMethod.GET, value="/scenarios/getAll")
    @CrossOrigin(origins = "*")
    public Iterable<ScenarioData> getScenarioAll() {
    	return scenarioRepository.findAll();
    }

    @RequestMapping(method=RequestMethod.GET, value="/scenarios/getScenario/{scenarioId}")
    @CrossOrigin(origins = "*")
    public ScenarioData getScenario(@PathVariable String scenarioId) {
        ScenarioData scenario = scenarioRepository.findByScenarioId(scenarioId);
        if(scenario == null) {
            throw new RuntimeException("Scenario with scenarioId='" + scenarioId + "' not found!");
        }
        return scenario;
    }

    // body:   An object representing a ScenarioData set
    // return:  saved record
    @RequestMapping(method = RequestMethod.POST, value = "/scenarios/saveScenario")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ScenarioData saveScenario(@RequestBody ScenarioData json) {
        ScenarioData record = (ScenarioData) scenarioRepository.save(json);
        return record;    
    }

    // body:   An object representing a ScenarioData set
    // return:  updated record
    @RequestMapping(method = RequestMethod.POST, value = "/scenarios/updateScenario")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ScenarioData updateScenario(@RequestBody ScenarioData json) {
        String scenarioId = json.getScenarioId();
        ScenarioData oldRecord = scenarioRepository.findByScenarioId(scenarioId);
        if(oldRecord == null) {
            throw new RuntimeException("Scenario with scenarioId='" + scenarioId + "' not found!");
        }
        ScenarioData newRecord = (ScenarioData) scenarioRepository.save(json);
        return newRecord;
    }

    // body:   A scenarioId of the scenario to be deleted
    // return:  
    @RequestMapping(method = RequestMethod.POST, value = "/scenarios/deleteScenario")
    @CrossOrigin(origins = "*")
    public void deleteScenario(@RequestBody ScenarioIdInput json) {
        String scenarioId = json.getScenarioId();
        ScenarioData record = scenarioRepository.findByScenarioId(scenarioId);
        if(record == null) {
            throw new RuntimeException("Scenario with scenarioId='" + scenarioId + "' not found!");
        }
        scenarioRepository.deleteByScenarioId(scenarioId);
    }

    // body:
    // return:  
    @RequestMapping(method = RequestMethod.POST, value = "/scenarios/deleteScenarioAll")
    @CrossOrigin(origins = "*")
    public void deleteScenarioAll() {
        scenarioRepository.deleteAll();
    }

}
