package org.actus.webapp.controllers;

import org.actus.webapp.models.Event;
import org.actus.webapp.models.EventStream2;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.ScenarioData;
import org.actus.webapp.models.ScenarioSimulationInput;
import org.actus.webapp.models.TwoDimensionalPrepaymentModelData;
import org.actus.webapp.models.TwoDimensionalCreditLossModelData;
import org.actus.webapp.models.TwoDimensionalDepositTrxModelData;
import org.actus.webapp.repositories.ScenarioRepository;
import org.actus.webapp.utils.TimeSeriesModel;
import org.actus.webapp.utils.TwoDimensionalPrepaymentModel;
import org.actus.webapp.utils.TwoDimensionalCreditLossModel;
import org.actus.webapp.utils.TwoDimensionalDepositTrxModel;
import org.actus.webapp.utils.MultiDimensionalRiskFactorModel;
import org.actus.webapp.core.functions.POF_PP;
import org.actus.webapp.core.functions.STF_PP;
import org.actus.webapp.core.functions.POF_CL;
import org.actus.webapp.core.functions.STF_CL;
import org.actus.webapp.core.functions.POF_CW;
import org.actus.webapp.core.functions.STF_CW;

import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.states.StateSpace;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.events.EventFactory;
import org.actus.events.ContractEvent;
import org.actus.time.ScheduleFactory;
import org.actus.events.EventFactory;
import org.actus.functions.pam.POF_AD_PAM;
import org.actus.functions.pam.STF_AD_PAM;
import org.actus.types.EventType;
import org.actus.types.ContractTypeEnum;
import org.actus.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.Period;

@RestController
public class SimulationController {

    @Autowired
    ScenarioRepository scenarioRepository;

    // param:   Json Array of Json Objects
    // return:  ArrayList of ArrayList of ContractEvents
    @RequestMapping(method = RequestMethod.POST, value = "/simulations/runScenario")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public List<EventStream2> runScenarioSimulation(@RequestBody ScenarioSimulationInput json) {
        
        System.out.println("****fnp001 Started a scenario simulation");  // fnp diagnostic jan 2023 
        // extract body parameters
        String scenarioId = json.getScenarioId();
        List<Map<String, Object>> contractData = json.getContracts();
        LocalDateTime simulateTo = json.getSimulateTo();
        Set<LocalDateTime> monitoringTimes = json.getMonitoringTimes();

        // fetch scenario data and create risk factor observer
        ScenarioData scenario = scenarioRepository.findByScenarioId(scenarioId);
        if(scenario == null) {
            throw new RuntimeException("Scenario with scenarioId='" + scenarioId + "' not found!");
        }
        RiskFactorModelProvider observer;
        try {
            observer = createObserver(scenario);
        } catch(Exception e){
            throw new RuntimeException("Could not create 'observer' for scenarioId='" + scenarioId + "'!");
        }

        // for each contract compute events
        ArrayList<EventStream2> output = new ArrayList<>();
        contractData.forEach(entry -> {
            // extract contract terms
            ContractModel terms;
            String contractID = (entry.get("contractID") == null)? "NA":entry.get("contractID").toString();
            try {
                terms = ContractModel.parse(entry);
                if(entry.get("auxiliaryTerms")!=null) {
                    ((List<HashMap<String,String>>) entry.get("auxiliaryTerms")).forEach(term -> terms.addAttribute(term.get("id"),term.get("value")));
                }
            } catch(Exception e){
                output.add(new EventStream2(scenarioId, contractID, "Failure", e.toString(), new ArrayList<Event>()));
                return; // skip this iteration and continue with next
            }
            // compute contract events
            try {
                output.add(new EventStream2(scenarioId, contractID, "Success", "", computeEvents(terms, observer, simulateTo, monitoringTimes, scenario)));
            }catch(Exception e){
                output.add(new EventStream2(scenarioId, contractID, "Failure", e.toString(), new ArrayList<Event>()));
            }
        });
        return output;
    }

    private RiskFactorModelProvider createObserver(ScenarioData json) {
        MultiDimensionalRiskFactorModel observer = new MultiDimensionalRiskFactorModel();
        List<ObservedData> timeSeriesData = json.getTimeSeriesData();
        List<TwoDimensionalPrepaymentModelData> prepModelData = json.getTwoDimensionalPrepaymentModelData();
        List<TwoDimensionalCreditLossModelData> creditLossModelData = json.getTwoDimensionalCreditLossModelData();
	List<TwoDimensionalDepositTrxModelData> depositTrxModelData = json.getTwoDimensionalDepositTrxModelData();

        if(timeSeriesData.size()>0) {
            timeSeriesData.forEach(entry -> {
                observer.add(entry.getMarketObjectCode(),new TimeSeriesModel(entry));
            });
        }
        
        if(!prepModelData.isEmpty()) {
            prepModelData.forEach(entry -> {
                try {
                    
                    observer.add(entry.getRiskFactorId(),
                        new TwoDimensionalPrepaymentModel(entry.getRiskFactorId(),entry,observer));
System.out.println("****fnp002 Added 2DPPmodel <"+ entry.getRiskFactorId()+"> to observer");  // fnp diagnostic jan 2023
                } catch(Exception e) {
                    throw new RuntimeException("riskFactorType for TwoDimensionalPrepaymentModelData with riskFactorId='" + entry.getRiskFactorId() + "' unsupported!");
                }
            });
        }

        if(!creditLossModelData.isEmpty()) {
            creditLossModelData.forEach(entry -> {
                try {
                    
                    observer.add(entry.getRiskFactorId(),
                        new TwoDimensionalCreditLossModel(entry.getRiskFactorId(),entry,observer));

                } catch(Exception e) {
                    throw new RuntimeException("riskFactorType for TwoDimensionalCreditLossModelData with riskFactorId='" + entry.getRiskFactorId() + "' unsupported!");
                }
            });
        }

        if(!depositTrxModelData.isEmpty()) {
            depositTrxModelData.forEach(entry -> {
                try {

                    observer.add(entry.getRiskFactorId(),
                        new TwoDimensionalDepositTrxModel(entry.getRiskFactorId(),entry));
                        // market data never needed for DepositTrx RF so observer parameter not passed this case
System.out.println("****fnp025 Added 2DDepositTrxModel <"+ entry.getRiskFactorId()+"> to observer");  // fnp diagnostic jan 2023
                } catch(Exception e) {
                    throw new RuntimeException("riskFactorType for TwoDimensionalDepositTrxModelData with riskFactorId='" + entry.getRiskFactorId() + "' unsupported!");
                }
            });
        }


        return observer;
    }

    private List<Event> computeEvents(ContractModel model, RiskFactorModelProvider observer, LocalDateTime to, 
                                    Set<LocalDateTime> monitoringTimes, ScenarioData scenario) {

        // define simulation horizon if not provided
        if(to == null) to = model.getAs("MaturityDate");
        if(to == null) to = LocalDateTime.now().plusYears(5);

        // compute actus schedule
        ArrayList<ContractEvent> schedule = ContractType.schedule(to, model);

        // add monitoring events if defined
        if(monitoringTimes != null && monitoringTimes.size()>0) {
            schedule.addAll(EventFactory.createEvents(
                        monitoringTimes,
                        EventType.AD,
                        model.getAs("Currency"),
                        new POF_AD_PAM(),
                        new STF_AD_PAM(),
                        model.getAs("BusinessDayConvention"),
                        model.getAs("ContractID")
                ));
        }

System.out.println("****fnp003 Adding events for contract<"+ model.getAs("ContractID") + ">");       // fnp diagnostic jan 2023
System.out.println("****fnp004 PPmodel ObjCd = <"+ model.getAs("ObjectCodeOfPrepaymentModel") + ">");  // fnp diagnostic jan 2023

        // add prepayment events if prepayment model referenced by contract
        if(!CommonUtils.isNull(model.getAs("ObjectCodeOfPrepaymentModel"))) {
            String modelId = model.getAs("ObjectCodeOfPrepaymentModel");
            TwoDimensionalPrepaymentModelData modelData = scenario.getTwoDimensionalPrepaymentModelData().
                    stream().filter(dat -> dat.getRiskFactorId().equals(modelId)).collect(Collectors.toList()).get(0);
            List<String> prepaymentEventTimes = modelData.getPrepaymentEventTimes();
            IntStream stream = IntStream.range(0,prepaymentEventTimes.size()); // start inclusive, end exclusive
            ArrayList<ContractEvent> prepaymentEvents = new ArrayList<ContractEvent>();
            stream.forEach(i -> prepaymentEvents.add(EventFactory.createEvent(
                LocalDateTime.parse(prepaymentEventTimes.get(i)),
                EventType.PP,
                model.getAs("Currency"),
                new POF_PP(),
                new STF_PP(),
                model.getAs("BusinessDayConvention"),
                model.getAs("ContractID")
            )));
            schedule.addAll(prepaymentEvents);
System.out.println("****fnp005 Returning from prepayment");       // fnp diagnostic jan 2023
        }
System.out.println("****fnp026 DepositTrxModel ObjCd = <"+ model.getAs("ObjectCodeOfCashBalanceModel") + ">");  // fnp diagnostic jan 2023
        // add deposit withdrawal events if withdrawal model referenced by UMP contract
        if(model.getAs("ContractType").equals(ContractTypeEnum.UMP) && !CommonUtils.isNull(model.getAs("ObjectCodeOfCashBalanceModel"))) {
	    String modelId = model.getAs("ObjectCodeOfCashBalanceModel");
System.out.println("****fnp027 Contract ObjectCodeOfCash Balance =<"+ modelId + ">");       // fnp diagnostic jan 2023
            TwoDimensionalDepositTrxModelData modelData = scenario.getTwoDimensionalDepositTrxModelData().
                    stream().filter(dat -> dat.getRiskFactorId().equals(modelId)).collect(Collectors.toList()).get(0);
            List<String> transactionEventTimes = modelData.getDepositTrxEventTimes();
            IntStream stream = IntStream.range(0,transactionEventTimes.size()); // start inclusive, end exclusive
            ArrayList<ContractEvent> transactionEvents = new ArrayList<ContractEvent>();
            stream.forEach(i -> transactionEvents.add(EventFactory.createEvent(
                LocalDateTime.parse(transactionEventTimes.get(i)),
                EventType.PR,
                model.getAs("Currency"),
                new POF_CW(),
                new STF_CW(),
                model.getAs("BusinessDayConvention"),
                model.getAs("ContractID")
            )));
            schedule.addAll(transactionEvents);
System.out.println("****fnp028 Returning from deposit Trx event scheduling");       // fnp diagnostic jan 2023
        }

System.out.println("****fnp006 Withdrawal processing passed");       // fnp diagnostic jan 2023

        // add credit loss events if credit loss model referenced by contract
        if(!CommonUtils.isNull(model.getAs("ObjectCodeOfCreditLossModel"))) {
            String modelId = model.getAs("ObjectCodeOfCreditLossModel");
            TwoDimensionalCreditLossModelData modelData = scenario.getTwoDimensionalCreditLossModelData().
                    stream().filter(dat -> dat.getRiskFactorId().equals(modelId)).collect(Collectors.toList()).get(0);
            List<String> creditEventTimes = modelData.getCreditEventTimes();
            List<Double> creditEventWeights = modelData.getCreditEventWeights();
            IntStream stream = IntStream.range(0,creditEventTimes.size()); // start inclusive, end exclusive
            ArrayList<ContractEvent> creditEvents = new ArrayList<ContractEvent>();
            stream.forEach(i -> creditEvents.add(EventFactory.createEvent(
                LocalDateTime.parse(creditEventTimes.get(i)),
                EventType.CE,
                model.getAs("Currency"),
                new POF_CL(creditEventWeights.get(i)),
                new STF_CL(creditEventWeights.get(i)),
                model.getAs("BusinessDayConvention"),
                model.getAs("ContractID")
            )));
            schedule.addAll(creditEvents);
        }

System.out.println("****fnp007 Credit loss processing passed");       // fnp diagnostic jan 2023
        // apply schedule to contract
        schedule = ContractType.apply(schedule, model, observer);

System.out.println("****fnp008 ContractType.apply passed");       // fnp diagnostic jan 2023      
        // transform schedule to event list and return
        return schedule.stream().map(e -> new Event(e)).collect(Collectors.toList());
    }

}
