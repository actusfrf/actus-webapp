import React, { PureComponent } from 'react';
import { Redirect } from 'react-router-dom'
import { Col, Grid, Row}  from 'react-bootstrap';
import axios from 'axios';
import Demo from '../Demo';

import ToolTip from '../ToolTip';
import '../Term/Term.css';
/* eslint-disable */
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import './Form.css';

export class Form extends PureComponent {
    state = {
        groups: [],
        error: false,
        startDate: new Date(),
        validated: false,
        optionalFields: [],
        mandatoryFields: [],
        originalRequiredFields:{},
        originalNonRequiredFields:{},
        requiredFields:{},
        nonRequiredFields:{},
        demos:[],
        showDemos: false,
        showForm: true,
        showOptionals: false,
        optionHidden:[],
        totalFields: 0,
        groupDescription: "",
        contractType: "",
        identifier: "",
        results:{},
        isFetching: false,
        redirect: false,
        host: "http://localhost:8080", //"http://marbella.myftp.org:8080"
        backFromResults: false,
        allAnswers: {}
    }

    getTestFields(){
        var t = {
            "ContractType": "PAM",
            "StatusDate": "2015-01-01T00:00:00",
            "ContractRole": "RPA",
            "ContractID": 101,
            "LegalEntityIDCounterparty": "TEST_LEI_CP",
            "NominalInterestRate": 0,
            "DayCountConvention": "30E/360",
            "CyclePointOfInterestPayment": 1,
            "Currency": "USD",
            "ContractDealDate": "2015-01-01T00:00:00",
            "InitialExchangeDate": "2015-01-02T00:00:00",
            "MaturityDate": "2015-04-02T00:00:00",
            "NotionalPrincipal": 1000,
            "RateSpread": 0,
            "CyclePointOfRateReset": 0,
            "RateMultiplier": 1,
            "MarketValueObserved": 10,
            "PremiumDiscountAtIED": -5
        }
        return t;
    }

    componentDidMount() {
        console.log('Did Mount');
        let {match} = this.props;
        if(this.props.location.state && this.props.location.state.backFromResults){
            console.log("[Data incoming]", this.props.location.state.backFromResults);
            this.fetchTerms(match.params.id, this.props.location.state.allAnswers);
        }else{
            this.fetchTerms(match.params.id);
        }
    }

    handleReset(e) {
        let requiredFieldCopy = {...this.state.requiredFields};
        let nonRequiredFieldsCopy = {...this.state.nonRequiredFields};
        for(var n in requiredFieldCopy){
            requiredFieldCopy[n] = ""
        }
        for(var n in nonRequiredFieldsCopy){
            nonRequiredFieldsCopy[n] = ""
        }
        this.setState({
            requiredFields: requiredFieldCopy,
            nonRequiredFields: nonRequiredFieldsCopy 
        })
    }

    cleanUpData(obj){

        let newObj={};

        for(var prop in obj){
            if(obj[prop] === ''){
                delete obj[prop];
            }
        }

        newObj = obj;
        return newObj;
    }

    handleSubmit(e) {
        e.preventDefault();
        let allAnswers = Object.assign({},this.state.requiredFields, this.state.nonRequiredFields);
        
        let requiredFields = allAnswers;
        let config = {
            'mode': 'cors',
            'headers': {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
            },
            'withCredentials': true,
            'credentials': 'omit'
        }
        let dataToSend = {...allAnswers};
        this.setState({
            allAnswers: this.cleanUpData(dataToSend)
        });

        axios.post(this.state.host+'/events', this.cleanUpData(dataToSend))
            .then(res => {
                this.setState({
                    results: res.data,
                    isFetching: false,
                    redirect: true,
                })
            })
            .catch(error => {
                console.log(this);
                console.log(error.message);
                
                this.setState({
                    error: error
                })
            });
    }

    validateFields(){
        let fields = this.state.requiredFields;
        let errorCount = 0;
        for(var f in fields){
            if(fields[f] === ''){
                errorCount++;
            }
        }

         this.setState({
             validated: errorCount === 0
         });
    }

    updateNonRequiredField(e) {
        this.setState({
            nonRequiredFields: {
                ...this.state.nonRequiredFields,
                [e.target.id]: e.target.value
            }
        });
    }

    updateField(e) {
        this.setState({
            requiredFields: {
                ...this.state.requiredFields,
                [e.target.id]: e.target.value
            }
        });
        
        this.validateFields();
    }

    fetchTerms(id, incoming){
        console.log("fetch data", id," is incoming ", incoming );

        this.setState({
            isFetching: true,
            allAnswers: incoming ? {...incoming}: null,
        });       

        axios.get(`/data/actus-dictionary.json`)
            .then(res => {
                if (!res || !res.data) {
                    return false;
                }

                // get contract identifier from id (accronym)
                let identifier = Object.keys(res.data.taxonomy).filter(key => (res.data.taxonomy[key].accronym.indexOf(id) > -1))[0];
                
                // get taxonomy, applicability and terms lists for respective contract
                let applicability = res.data.applicability[identifier]
                let terms = res.data.terms
                let taxonomy = res.data.taxonomy[identifier]
                
                // get optional and mandatory field identifiers
                let optionalFieldIdentifiers = Object.keys(applicability).filter(key => (applicability[key] !== 'NN'));
                let mandatoryFieldIdentifiers = Object.keys(applicability).filter(key => (applicability[key] === 'NN'));
                optionalFieldIdentifiers.splice(optionalFieldIdentifiers.indexOf('contract'),1) // 'contract' is actually not an official term

                // get optional and mandatory field details
                let optionalFields = optionalFieldIdentifiers.map((identifier) => terms[identifier])
                let mandatoryFields = mandatoryFieldIdentifiers.map((identifier) => terms[identifier])

                // define optional and mandatory field values (empty by default)
                let requiredFields = Object.assign({}, ...mandatoryFields.map(o=>({[o.name]: ''})));
                let nonRequiredFields = Object.assign({}, ...optionalFields.map(o=>({[o.name]: ''}))); 
                
                // if returning from the results page, populate previous field values
                if(incoming){
                    mandatoryFieldIdentifiers.map(e=>{
                        requiredFields[e] = incoming[e];
                    });

                    optionalFieldIdentifiers.map(e=>{
                        nonRequiredFields[e] = incoming[e];
                    });
                }

                // group terms according to actus groups
                let groupToValues = optionalFields.reduce(function (obj, item) { 
                        obj[item.group] = obj[item.group] || [];
                        obj[item.group].push(item);
                        return obj;
                    }, Object.create(null));

                let groups = Object
                    .keys(groupToValues)
                    .map(function (key) {
                        let shortName = key.split(" ").join("");
                        return {
                            group: key, 
                            Items: groupToValues[key], 
                            shortName: shortName,
                            visible: false
                        }
                    });
                
                // fetch demos for this specific contract
                this.fetchDemos(id);

                // add data to state
                this.setState({
                    groups: groups,
                    isFetching:false,
                    optionalFields: [...optionalFields],
                    mandatoryFields: [...mandatoryFields],
                    requiredFields: {...requiredFields},
                    nonRequiredFields: {...nonRequiredFields},
                    originalRequiredFields: {...mandatoryFieldIdentifiers},
                    originalNonRequiredFields: {...optionalFieldIdentifiers},
                    groupDescription: taxonomy.description,
                    contractType: taxonomy.accronym,
                    identifier: identifier,
                    error: {
                        ...this.state.error
                    }
                });
            })
            .catch(error => {
                console.log('>>>>>>>>>>> error:', error);
            });
    }  

    fetchDemos(id){
        axios
            .get(`${this.state.host}/demos/${id}`)
            .then(res => {
                this.setState({
                    demos: res.data
                });
            })
            .catch(error => {
                console.log('>>>>>>>>>>> error:', error);
            })
    }

    toggleDemos() {
        this.setState({
            showDemos: !this.state.showDemos
        })
    }

    toggleForm() {
        this.setState({
            showForm: !this.state.showForm
        })
    }

    passDemoData(terms, id) {
        let groups = [...this.state.groups];
        let nonRequired = {...this.state.originalNonRequiredFields};
        let required = {...this.state.originalRequiredFields};

        let termArray = Object.entries(terms);
        let requiredArray = Object.entries(required);

        if(this.state.showDemos)
            this.toggleDemos();

        if(!this.state.showForm)
            this.toggleForm();

        // assign the required terms from the demo to the "required" state
        termArray.map(t=>{
            requiredArray.map(r=>{
                if(t[0] == r[1]){
                    required[t[0]] = t[1];
                }
            });
        });

        // for each terms group assign the optional terms to the "nonRequired" state
        groups.map(g =>{
            g.Items.map(i => {
               termArray.map(t=>{
                   if(t[0] == i.identifier){
                        nonRequired[t[0]] = t[1];
                   }
               });
            });
        });

        // update the state
        this.setState({
            requiredFields: {
                ...this.state.originalRequiredFields,
                ...required},
            nonRequiredFields: {
                ...this.state.originalNonRequiredFields,
                ...nonRequired}
        });

    }

    foldOptions(index) {
        var groups = [...this.state.groups];
        groups[index].visible = !groups[index].visible;
        this.setState({
            groups: groups
        });
    }

    render() {
        let {groups, groupDescription, contractType, identifier, mandatoryFields, redirect, results, demos, error} = this.state;
        let {match} = this.props;
        let demosClassName = (this.state.showDemos)?"unfolded":"folded";
        let formClassName = (this.state.showForm)?"unfolded":"folded";

        if( redirect ) {
            return <Redirect to={{ pathname: '/results', state: { url:`/form/${match.params.id}`, allAnswers: this.state.allAnswers, contractId: this.state.requiredFields.contractID, data: results }}} />
        } else {  
            if(this.state.isFetching){
                return (
                    <div>Loading...</div>
                )
            } else {
                return (
                    <div id="form-container" identifier={ identifier } >
                        <Grid fluid>
                            <Row>
                                <Col sm={12} className={`contract-main-wrapper demo ${demosClassName}`} onClick={()=>this.toggleDemos()}>
                                    <span className="contract-title">Demo Case</span>
                                </Col>
                            </Row>
                            {this.state.showDemos && 
                                <Row>
                                    <Col sm={12} className="demo-items-wrapper">
                                        <Grid fluid>
                                            <Row className="demo-row">
                                            {demos.sort((a, b) => a.terms.contractID - b.terms.contractID).map((d, i)=> {
                                                    return (
                                                        <Demo key={i} passDemoData={this.passDemoData.bind(this)} description={d.description} identifier={d.identifier} index={d.terms.contractID} demoId={d.id} label={d.label} terms={d.terms}/>
                                                    )
                                                })}
                                            </Row>
                                        </Grid>
                                    </Col>
                                </Row>
                            }
                            <Row>
                                <Col sm={12} className={`contract-main-wrapper ${formClassName}`} onClick={()=>this.toggleForm()}>
                                    <span className="contract-title">{contractType}:</span>
                                    <span className="contract-description">{groupDescription}</span>
                                </Col>
                            </Row>
                            {this.state.showForm && 
                                <Row>
                                <Col sm={5} className="required choices">
                                    <div className="term-group-header">All fields below are mandatory to fill in:</div>
                                    <div className="field-wrapper">
                                        <div className="items-group">
                                            <Grid fluid>
                                                <Row>
                                                    {mandatoryFields.map((m, groupId) => {
                                                            // for each mandatory term add a field in the form and
                                                            // preset with value from demo case (if any)
                                                            return (                                                        
                                                                <Col key={`term_wrapper${groupId}`} sm={6} className="item nopadding">
                                                                    <div className="input-container">
                                                                        <label className="item-labels" htmlFor={m.name}>{m.name}</label>
                                                                        <div className="input-wrapper">
                                                                            <input 
                                                                            id={m.identifier}
                                                                            title={`Required Field`} 
                                                                            placeholder='...' 
                                                                            value={this.state.requiredFields[m.identifier]}
                                                                            onChange={e=>this.updateField(e)}
                                                                            className="item-fields" 
                                                                            type="text" />
                                                                            <ToolTip description={m.description} />
                                                                        </div>                                        
                                                                    </div>
                                                                </Col>                                                                    
                                                            )
                                                        })
                                                    }
                                                </Row>
                                            </Grid>
                                        </div>
                                    </div>
                                </Col>
                                <Col sm={7} className="optional choices">
                                    <div className="term-group-header">Below are your Optional choices</div>
                                    {
                                        groups.map((group, index) => {
                                            // go through all term groups and create separate optional field tab
                                            return (
                                                <div key={`term_wrapper${index}`} className="term-wrapper">
                                                    <div id={group.shortName} className="items-group">
                                                        <div className="item-header" onClick={()=>this.foldOptions(index)}>{group.group}</div>
                                                        {group.visible && 
                                                            <Grid fluid>
                                                                <Row>
                                                                {
                                                                    group.Items.map((item, index) => {
                                                                        // for a specific term group go through all terms and create
                                                                        // a form field
                                                                        let group = item.group;

                                                                        return(
                                                                            <Col key={`key${item.identifier}`} sm={4} className="item nopadding">
                                                                                <div className="input-container">
                                                                                    <label className="item-labels" htmlFor={item.name}>{item.name}</label>
                                                                                    <div className="input-wrapper term">
                                                                                        <input 
                                                                                        id={item.identifier} 
                                                                                        group={group}
                                                                                        title={`Optional Choice`} 
                                                                                        placeholder={`...`}
                                                                                        value={this.state.nonRequiredFields[item.identifier]}
                                                                                        onChange={e=>this.updateNonRequiredField(e)}
                                                                                        className="item-fields"
                                                                                        type="text" />
                                                                                        <ToolTip description={item.description} />
                                                                                    </div>                                        
                                                                                </div>
                                                                            </Col>
                                                                        )
                                                                    })
                                                                }
                                                                </Row>
                                                            </Grid>
                                                        }
                                                    </div>
                                                </div>
                                            )
                                        })
                                    }
                                </Col>
                            </Row>
                            }
                            <Row>
                                <Col sm={6} className="text-right">
                                    <input type="reset" value="RESET" onClick={(e) => this.handleReset(e)}/>
                                </Col>
                                <Col sm={6} className={(this.state.validated)?`text-left`:`text-left`}>
                                    <input type="submit" value="SEND" onClick={(e) => this.handleSubmit(e)}/>
                                </Col>
                            </Row>
                        </Grid>
                    </div>
                );
            }
        }
    }
}
