import React, { PureComponent } from 'react';
import { Redirect } from 'react-router-dom'
import { Col, Grid, Row}  from 'react-bootstrap';
import axios from 'axios';
import Demo from '../Demo';

import ToolTip from '../ToolTip';
import '../Term/Term.css';

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
        riskFactorData: [],
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
        host: "http://localhost:8080",
        backFromResults: false,
        allAnswers: {},
        // handling underlyings (capfl, ...)
        underlyingType: "",
        hasUnderlying: false,
        showUnderlying: true,
        showLeg1: true,
        showLeg2: true,
        underlyingTypes: ["PAM","NAM","ANN","LAM","LAX"],
        underlyingGroups: [],
        underlyingOptionalFields: [],
        underlyingMandatoryFields: [],
        underlyingRequiredFields: {},
        underlyingNonRequiredFields: {},
        underlyingOriginalRequiredFields: {},
        underlyingOriginalNonRequiredFields: {},
        // handling leg1 (swap)
        leg1Groups: [],
        leg1OptionalFields: [],
        leg1MandatoryFields: [],
        leg1RequiredFields: {},
        leg1NonRequiredFields: {},
        leg1OriginalRequiredFields: {},
        leg1OriginalNonRequiredFields: {},
        // handling leg2 (swap)
        leg2Groups: [],
        leg2OptionalFields: [],
        leg2MandatoryFields: [],
        leg2RequiredFields: {},
        leg2NonRequiredFields: {},
        leg2OriginalRequiredFields: {},
        leg2OriginalNonRequiredFields: {}
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
            nonRequiredFields: nonRequiredFieldsCopy,
            riskFactorData: []
        })
    }

    cleanUpTerms(obj){

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
        let termsToSend = this.cleanUpTerms({...allAnswers});
        this.setState({
            allAnswers: termsToSend
        });

        let dataToSend = { 
            contract: termsToSend,
            riskFactors: this.state.riskFactorData
        };

        axios.post(this.state.host+'/events', dataToSend)
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

    handleExport (e) {
        e.preventDefault();
        let allAnswers = Object.assign({},this.state.requiredFields, this.state.nonRequiredFields);
        let data = JSON.stringify({...allAnswers});
        var file = new Blob([data], {type: 'application/json'});
        if (window.navigator.msSaveOrOpenBlob) // IE10+
            window.navigator.msSaveOrOpenBlob(file, 'terms.json');
        else { // Others
            let a = document.createElement('a');
            let url = URL.createObjectURL(file);
            a.href = url;
            a.download = 'terms.json';
            document.body.appendChild(a);
            a.click();
            setTimeout(function() {
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            }, 0);
        }
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

    handleChangeUnderlyingType = (e) => {
        let type = e.target.value
        // fetch dictionary
        axios.get(`/data/actus-dictionary.json`)
        .then(res => {
            if (!res || !res.data) {
                return false;
            }
            // extract required terms from dictionary
            let terms = this.collectTerms(res.data,type,false);
            // update state
            this.setState({
                isFetching: false,
                underlyingType: type,
                underlyingGroups: terms.groups,
                underlyingOptionalFields: [...terms.optionalFields],
                underlyingMandatoryFields: [...terms.mandatoryFields],
                underlyingRequiredFields: {...terms.requiredFields},
                underlyingNonRequiredFields: {...terms.nonRequiredFields},
                underlyingOriginalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                underlyingOriginalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                error: {
                    ...this.state.error
                }
            });
        });
    }

    handleChangeLeg1Type = (e) => {
        let type = e.target.value
        // fetch dictionary
        axios.get(`/data/actus-dictionary.json`)
        .then(res => {
            if (!res || !res.data) {
                return false;
            }
            // extract required terms from dictionary
            let terms = this.collectTerms(res.data,type,false);
            // update state
            this.setState({
                isFetching: false,
                leg1Type: type,
                leg1Groups: terms.groups,
                leg1OptionalFields: [...terms.optionalFields],
                leg1MandatoryFields: [...terms.mandatoryFields],
                leg1RequiredFields: {...terms.requiredFields},
                leg1NonRequiredFields: {...terms.nonRequiredFields},
                leg1OriginalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                leg1OriginalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                error: {
                    ...this.state.error
                }
            });
        });
    }

    handleChangeLeg2Type = (e) => {
        let type = e.target.value
        // fetch dictionary
        axios.get(`/data/actus-dictionary.json`)
        .then(res => {
            if (!res || !res.data) {
                return false;
            }
            // extract required terms from dictionary
            let terms = this.collectTerms(res.data,type,false);
            // update state
            this.setState({
                isFetching: false,
                leg2Type: type,
                leg2Groups: terms.groups,
                leg2OptionalFields: [...terms.optionalFields],
                leg2MandatoryFields: [...terms.mandatoryFields],
                leg2RequiredFields: {...terms.requiredFields},
                leg2NonRequiredFields: {...terms.nonRequiredFields},
                leg2OriginalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                leg2OriginalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                error: {
                    ...this.state.error
                }
            });
        });
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

    toggleUnderlying() {
        this.setState({
            showUnderlying: !this.state.showUnderlying
        })
    }

    toggleLeg1() {
        this.setState({
            showLeg1: !this.state.showLeg1
        })
    }

    toggleLeg2() {
        this.setState({
            showLeg2: !this.state.showLeg2
        })
    }

    foldOptions(index) {
        var groups = [...this.state.groups];
        groups[index].visible = !groups[index].visible;
        this.setState({
            groups: groups
        });
    }

    foldUnderlyingOptions(index) {
        var groups = [...this.state.underlyingGroups];
        groups[index].visible = !groups[index].visible;
        this.setState({
            underlyingGroups: groups
        });
    }

    foldLeg1Options(index) {
        var groups = [...this.state.leg1Groups];
        groups[index].visible = !groups[index].visible;
        this.setState({
            leg1Groups: groups
        });
    }

    foldLeg2Options(index) {
        var groups = [...this.state.leg2Groups];
        groups[index].visible = !groups[index].visible;
        this.setState({
            leg2Groups: groups
        });
    }

    fetchTerms(id, incoming){
        console.log("fetch data", id," is incoming ", incoming );
        // indicate state that fetching data
        this.setState({
            isFetching: true,
            allAnswers: incoming ? {...incoming}: null,
        });       
        // fetch dictionary
        axios.get(`/data/actus-dictionary.json`)
            .then(res => {
                if (!res || !res.data) {
                    return false;
                }
                // extract required terms from dictionary
                let terms = this.collectTerms(res.data,id,incoming);
                // fetch demos for this specific contract
                this.fetchDemos(id);
                // update state
                this.setState({
                    identifier: terms.identifier,
                    groupDescription: terms.description,
                    contractType: terms.type,
                    groups: terms.groups,
                    isFetching: false,
                    optionalFields: [...terms.optionalFields],
                    mandatoryFields: [...terms.mandatoryFields],
                    requiredFields: {...terms.requiredFields},
                    nonRequiredFields: {...terms.nonRequiredFields},
                    originalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                    originalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                    error: {
                        ...this.state.error
                    }
                });
                // handle underlying contracts
                let type = "" // underlying default type
                switch(terms.type) {
                    case 'SWAPS':
                        type = "PAM" // default type
                        // leg 1
                        // -----
                        // extract underlying terms
                        terms = this.collectTerms(res.data,type,incoming);
                        // update state
                        this.setState({ 
                            hasUnderlying: true,
                            underlyingTypes: ["PAM","NAM","ANN","LAM","LAX"],
                            leg1Type: type,
                            leg1Groups: terms.groups,
                            leg1OptionalFields: [...terms.optionalFields],
                            leg1MandatoryFields: [...terms.mandatoryFields],
                            leg1RequiredFields: {...terms.requiredFields},
                            leg1NonRequiredFields: {...terms.nonRequiredFields},
                            leg1OriginalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                            leg1OriginalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                            error: {
                                ...this.state.error
                            }
                        });
                        // leg 2
                        // -----
                        // update state
                        this.setState({
                            leg2Type: type,
                            leg2Groups: terms.groups,
                            leg2OptionalFields: [...terms.optionalFields],
                            leg2MandatoryFields: [...terms.mandatoryFields],
                            leg2RequiredFields: {...terms.requiredFields},
                            leg2NonRequiredFields: {...terms.nonRequiredFields},
                            leg2OriginalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                            leg2OriginalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                            error: {
                                ...this.state.error
                            }
                        });
                        break;
                    case 'CAPFL': // has only one underlying
                        type = "PAM" // default type
                        // extract underlying terms
                        terms = this.collectTerms(res.data,type,incoming);
                        // update state
                        this.setState({ 
                            hasUnderlying: true,
                            underlyingType: type,
                            underlyingTypes: ["PAM","NAM","ANN","LAM","LAX"],
                            underlyingGroups: terms.groups,
                            underlyingOptionalFields: [...terms.optionalFields],
                            underlyingMandatoryFields: [...terms.mandatoryFields],
                            underlyingRequiredFields: {...terms.requiredFields},
                            underlyingNonRequiredFields: {...terms.nonRequiredFields},
                            underlyingOriginalRequiredFields: {...terms.mandatoryFieldIdentifiers},
                            underlyingOriginalNonRequiredFields: {...terms.optionalFieldIdentifiers},
                            error: {
                                ...this.state.error
                            }
                        });
                        break;

                    default: // no underlying
                        this.setState({ 
                            hasUnderlying: false
                        })
                        break;
                }
            })
            .catch(error => {
                console.log('>>>>>>>>>>> error:', error);
            });
    } 

    collectTerms(dictionary, id, incoming) {
        // get contract identifier from id (accronym)
        let identifier = Object.keys(dictionary.taxonomy).filter(key => (dictionary.taxonomy[key].acronym.indexOf(id) > -1))[0];
        
        // get taxonomy, applicability and terms lists for respective contract
        let applicability = dictionary.applicability[identifier]
        let terms = dictionary.terms
        
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

        // return data
        return {
            identifier: identifier,
            description: dictionary.taxonomy[identifier].description,
            type: dictionary.taxonomy[identifier].acronym,
            groups: groups, 
            optionalFields: optionalFields, 
            mandatoryFields: mandatoryFields, 
            requiredFields: requiredFields,
            nonRequiredFields: nonRequiredFields, 
            mandatoryFieldIdentifiers: mandatoryFieldIdentifiers, 
            optionalFieldIdentifiers: optionalFieldIdentifiers
        }
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

    passDemoData(terms, riskFactorData) {
        let groups = [...this.state.groups];
        let nonRequired = {...this.state.originalNonRequiredFields};
        let required = {...this.state.originalRequiredFields};

        let termArray = Object.entries(terms);
        let requiredArray = Object.entries(required);

        if(this.state.showDemos)
            this.toggleDemos();

        if(!this.state.showForm)
            this.toggleForm();

        if(!this.state.showUnderlying)
        this.toggleUnderlying();

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
                ...nonRequired},
            riskFactorData: riskFactorData
        });

    }

    render() {
        let {groups, groupDescription, contractType, identifier, mandatoryFields, redirect, results, demos, error} = this.state;
        let {underlyingGroups, underlyingMandatoryFields, leg1Groups, leg1MandatoryFields, leg2Groups, leg2MandatoryFields} = this.state;
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
                                            {demos.sort((a, b) => a.contract.contractID - b.contract.contractID).map((d, i)=> {
                                                    return (
                                                            <Demo key={i} passDemoData={this.passDemoData.bind(this)} description={d.description} identifier={d.identifier} index={d.contract.contractID} demoId={d.id} label={d.label} terms={d.contract} riskFactorData={d.riskFactors}/>
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
                            {this.state.hasUnderlying && this.state.contractType=="SWAPS" &&
                            <>
                            <Row>
                                <Col sm={12} className={`contract-main-wrapper ${formClassName}`} onClick={()=>this.toggleLeg1()}>
                                    <span className="contract-title">{"Leg 1"}:</span>
                                    <span className="contract-description">{"This contract constitutes Leg 1 of the Swap"}</span>
                                </Col>
                            </Row>
                            {this.state.showLeg1 && 
                                <Row>
                                    <Col sm={5} className="required choices">
                                        <div className="term-group-header">All fields below are mandatory to fill in:</div>
                                        <div className="field-wrapper">
                                            <div className="items-group">
                                                <Grid fluid>
                                                    <Row>
                                                        {leg1MandatoryFields.map((m, groupId) => {
                                                            if(m.identifier=="contractType") {
                                                                // add dropdown for selecting the contractType
                                                                return(
                                                                    <Col key={`term_wrapper${groupId}`} sm={6} className="item nopadding">
                                                                        <div className="input-container">
                                                                            <label className="item-labels" htmlFor={m.name}>{m.name}</label>
                                                                            <div className="input-wrapper">
                                                                                <select 
                                                                                    id={m.identifier}
                                                                                    title={`Required Field`} 
                                                                                    value={this.state.underlyingType} 
                                                                                    onChange={e=>this.handleChangeLeg1Type(e)} 
                                                                                    className="item-fields" >
                                                                                    {this.state.underlyingTypes.map(type => (
                                                                                        <option key={type} value={type}>
                                                                                            {type}
                                                                                        </option>
                                                                                    ))}
                                                                                </select>
                                                                            </div>                                        
                                                                        </div>
                                                                    </Col> 
                                                                )
                                                            } else {
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
                                                                                value={this.state.leg1RequiredFields[m.identifier]}
                                                                                onChange={e=>this.updateField(e)}
                                                                                className="item-fields" 
                                                                                type="text" />
                                                                                <ToolTip description={m.description} />
                                                                            </div>                                        
                                                                        </div>
                                                                    </Col>                                                                    
                                                                )
                                                            }
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
                                            leg1Groups.map((group, index) => {
                                                // go through all term groups and create separate optional field tab
                                                return (
                                                    <div key={`term_wrapper${index}`} className="term-wrapper">
                                                        <div id={group.shortName} className="items-group">
                                                            <div className="item-header" onClick={()=>this.foldLeg1Options(index)}>{group.group}</div>
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
                                                                                            value={this.state.leg1NonRequiredFields[item.identifier]}
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
                                <Col sm={12} className={`contract-main-wrapper ${formClassName}`} onClick={()=>this.toggleLeg2()}>
                                    <span className="contract-title">{"Leg 2"}:</span>
                                    <span className="contract-description">{"This contract constitutes Leg 2 of the Swap"}</span>
                                </Col>
                            </Row>
                            {this.state.showLeg2 && 
                                <Row>
                                    <Col sm={5} className="required choices">
                                        <div className="term-group-header">All fields below are mandatory to fill in:</div>
                                        <div className="field-wrapper">
                                            <div className="items-group">
                                                <Grid fluid>
                                                    <Row>
                                                        {leg2MandatoryFields.map((m, groupId) => {
                                                                if(m.identifier=="contractType") {
                                                                    // add dropdown for selecting the contractType
                                                                    return(
                                                                        <Col key={`term_wrapper${groupId}`} sm={6} className="item nopadding">
                                                                            <div className="input-container">
                                                                                <label className="item-labels" htmlFor={m.name}>{m.name}</label>
                                                                                <div className="input-wrapper">
                                                                                    <select 
                                                                                        id={m.identifier}
                                                                                        title={`Required Field`} 
                                                                                        value={this.state.underlyingType} 
                                                                                        onChange={this.handleChangeLeg2Type} 
                                                                                        className="item-fields" >
                                                                                        {this.state.underlyingTypes.map(type => (
                                                                                            <option key={type} value={type}>
                                                                                                {type}
                                                                                            </option>
                                                                                        ))}
                                                                                    </select>
                                                                                </div>                                        
                                                                            </div>
                                                                        </Col> 
                                                                    )
                                                                } else {
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
                                                                                    value={this.state.leg2RequiredFields[m.identifier]}
                                                                                    onChange={e=>this.updateField(e)}
                                                                                    className="item-fields" 
                                                                                    type="text" />
                                                                                    <ToolTip description={m.description} />
                                                                                </div>                                        
                                                                            </div>
                                                                        </Col>                                                                    
                                                                    )
                                                                }
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
                                            leg2Groups.map((group, index) => {
                                                // go through all term groups and create separate optional field tab
                                                return (
                                                    <div key={`term_wrapper${index}`} className="term-wrapper">
                                                        <div id={group.shortName} className="items-group">
                                                            <div className="item-header" onClick={()=>this.foldLeg2Options(index)}>{group.group}</div>
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
                                                                                            value={this.state.leg2NonRequiredFields[item.identifier]}
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
                            </>
                            }
                            {this.state.hasUnderlying && this.state.contractType!="SWAPS" &&
                            <>
                            <Row>
                                <Col sm={12} className={`contract-main-wrapper ${formClassName}`} onClick={()=>this.toggleUnderlying()}>
                                    <span className="contract-title">{"Underlying"}:</span>
                                    <span className="contract-description">{"This contract constitutes the underlying"}</span>
                                </Col>
                            </Row>
                            {this.state.showUnderlying && 
                                <Row>
                                    <Col sm={5} className="required choices">
                                        <div className="term-group-header">All fields below are mandatory to fill in:</div>
                                        <div className="field-wrapper">
                                            <div className="items-group">
                                                <Grid fluid>
                                                    <Row>
                                                        {underlyingMandatoryFields.map((m, groupId) => {
                                                            if(m.identifier=="contractType") {
                                                                // add dropdown for selecting the contractType
                                                                return(
                                                                    <Col key={`term_wrapper${groupId}`} sm={6} className="item nopadding">
                                                                        <div className="input-container">
                                                                            <label className="item-labels" htmlFor={m.name}>{m.name}</label>
                                                                            <div className="input-wrapper">
                                                                                <select 
                                                                                    id={m.identifier}
                                                                                    title={`Required Field`} 
                                                                                    value={this.state.underlyingType} 
                                                                                    onChange={this.handleChangeUnderlyingType} 
                                                                                    className="item-fields" >
                                                                                    {this.state.underlyingTypes.map(type => (
                                                                                        <option key={type} value={type}>
                                                                                            {type}
                                                                                        </option>
                                                                                    ))}
                                                                                </select>
                                                                            </div>                                        
                                                                        </div>
                                                                    </Col> 
                                                                )
                                                            } else {
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
                                                                                value={this.state.underlyingRequiredFields[m.identifier]}
                                                                                onChange={e=>this.updateField(e)}
                                                                                className="item-fields" 
                                                                                type="text" />
                                                                                <ToolTip description={m.description} />
                                                                            </div>                                        
                                                                        </div>
                                                                    </Col>                                                                    
                                                                )
                                                            }
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
                                            underlyingGroups.map((group, index) => {
                                                // go through all term groups and create separate optional field tab
                                                return (
                                                    <div key={`term_wrapper${index}`} className="term-wrapper">
                                                        <div id={"underlying-" + group.shortName} className="items-group">
                                                            <div className="item-header" onClick={()=>this.foldUnderlyingOptions(index)}>{group.group}</div>
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
                                                                                            value={this.state.underlyingNonRequiredFields[item.identifier]}
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
                            </>
                            }
                            <Row>
                                <Col sm={5} className="text-right">
                                </Col>
                                <Col sm={7} className={(this.state.validated)?`text-left`:`text-left`}>
                                    <input type="reset" value="RESET" className="mr-2" onClick={(e) => this.handleReset(e)}/>
                                    <input type="submit" value="SEND" className="mr-2" onClick={(e) => this.handleSubmit(e)}/>
                                    <input type="submit" value="EXPORT TERMS" onClick={(e) => this.handleExport(e)}/>
                                </Col>
                            </Row>
                        </Grid>
                    </div>
                );
            }
        }
    }
}
