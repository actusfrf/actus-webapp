import React, { PureComponent } from 'react';
import { Redirect } from 'react-router-dom'
import { Col, Grid, Row}  from 'react-bootstrap';
import { Term } from '../Term';
import axios from 'axios';
import Demo from '../Demo';

import ToolTip from '../ToolTip';
//import '../Term.css';
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
        totalFields: 0,
        groupDescription: "",
        contractType: "",
        identifier: "",
        version: "",
        results:{},
        isFetching: false,
        redirect: false,
        host: "http://190.141.20.26", //http://190.141.20.26/
    }

    assemble(a, b) {
        a.Group[b.Name] = b.Name;
        return a;
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

    handleChange(date) {
        this.setState({
            startDate: date
        });
    }

    handleReset(e) {
        console.log('Reset');
    }

    handleSubmit(e) {
        e.preventDefault();
        let allAnswers = Object.assign({},this.state.requiredFields, this.state.nonRequiredFields);
        
        let requiredFields = this.state.requiredFields;
        let config = {
            'mode': 'cors',
            'headers': {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
            },
            'withCredentials': true,
            'credentials': 'omit'
        }
        axios.post(this.state.host+'/events', requiredFields)
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

    onGroupUpdate(e){
        console.log(e.target.id);
        console.log(e.target.title);
        console.log(e.target.group);
    }
    
    componentDidMount() {
        console.log('Did Mount');
        let {match} = this.props;
        this.fetchTerms(match.params.id);
    }

    fetchTerms(id){
        this.setState({
            isFetching: true
        });

        axios
            .get(`${this.state.host}/terms/meta/${id}`)
            .then(res => {
                if (!res.data[0].terms || !res || !res.data) {
                    return false;
                }
                let responseData = res.data[0];
                console.log(responseData);

                let optionalFields = res.data[0].terms.filter(n => (n.applicability.indexOf('NN') <= -1));
                let mandatoryFields = res.data[0].terms.filter(n => (n.applicability.indexOf('NN') > -1));

                let requiredFields = Object.assign({}, ...mandatoryFields.map(o=>({[o.name]: ''})));
                let nonRequiredFields = Object.assign({}, ...optionalFields.map(o=>({[o.name]: ''})));

                let groupToValues = optionalFields.reduce(function (obj, item) { 
                        obj[item.group] = obj[item.group] || [];
                        obj[item.group].push(item);
                        return obj;
                    }, Object.create(null));

                let fields = {};

                //auto populate fields with values for testing
                optionalFields.map(function (term, index) {
                        fields[term.name] = {};
                        fields[term.name] = '';
                    });

                let groups = Object
                    .keys(groupToValues)
                    .map(function (key) {
                        //console.log(key);
                        return {group: key, Items: groupToValues[key]};
                    });

                this.fetchDemos(id);
                this.setState({
                    groups: groups,
                    isFetching:false,
                    optionalFields: [...optionalFields],
                    mandatoryFields: [...mandatoryFields],
                    requiredFields: {...requiredFields},
                    nonRequiredFields: {...nonRequiredFields},
                    originalRequiredFields: {...requiredFields},
                    originalNonRequiredFields: {...nonRequiredFields},
                    totalFields: Object.keys(fields).length,
                    groupDescription: responseData.description,
                    contractType: responseData.contractType,
                    identifier: responseData.identifier,
                    version: responseData.version,
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
            .get(`${this.state.host}/demos/meta/${id}`)
            .then(res => {
                this.setState({
                    demos: res.data
                });
                //console.log("demos", res.data);
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

        termArray.map(t=>{
            requiredArray.map(r=>{
                if(t[0] == r[0]){
                    required[t[0]] = t[1];
                }
            });
        });

        //console.log(terms);
        groups.map(g =>{
            //console.log(g);
            g.Items.map(i => {
               termArray.map(t=>{
                   if(t[0] == i.name){
                        nonRequired[t[0]] = t[1];
                   }
               });
            });
        });
        this.setState({
            requiredFields: {
                ...this.state.originalRequiredFields,
                ...required},
            nonRequiredFields: {
                ...this.state.originalNonRequiredFields,
                ...nonRequired}
        });

    }

    render() {
        let {groups, groupDescription, contractType, identifier, version, mandatoryFields, redirect, results, demos} = this.state;
        let demosClassName = (this.state.showDemos)?"unfolded":"folded";
        let formClassName = (this.state.showForm)?"unfolded":"folded";
        //let { match } = this.props;
        if( redirect ) {
            return <Redirect to={{ pathname: '/results', state: { data: results } }} />
        } else {  
            if(this.state.isFetching){
                return (
                    <div>Loading...</div>
                )
            } else {
                return (
                    <div id="form-container" identifier={ identifier } version={ version }>
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
                                            {demos.map((d, i)=> {
                                                    return (
                                                        <Demo key={i} passDemoData={this.passDemoData.bind(this)} description={d.description} identifier={d.identifier} index={i} demoId={d.id} label={d.label} terms={d.terms}/>
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
                                                            let itemName = m.name;
                                                            itemName = itemName.replace(/([a-z])([A-Z])/g, '$1 $2');
                                                            itemName = itemName.replace(/([A-Z])([A-Z])/g, '$1 $2');
        
                                                            return (                                                        
                                                                <Col key={`term_wrapper${groupId}`} sm={6} className="item nopadding">
                                                                    <div className="input-container">
                                                                        <label className="item-labels" htmlFor={m.name}>{itemName}</label>
                                                                        <div className="input-wrapper">
                                                                            <input id={m.name}
                                                                            applicability={m.applicability}
                                                                            title={`Required Field`} 
                                                                            placeholder='...' 
                                                                            value={this.state.requiredFields[m.name]}
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
                                            //console.log(group);
                                            return (
                                                <div key={`term_wrapper${index}`} className="term-wrapper">
                                                    <div id={group} className="items-group">
                                                        <div className="item-header">{group.group}</div>
                                                        <Grid fluid>
                                                            <Row>
                                                            {
                                                                group.Items.map((item, index) => {
                                                                    let itemName = item.name;
                                                                    let group = item.group;
                                                                    itemName = itemName.replace(/([a-z])([A-Z])/g, '$1 $2');
                                                                    itemName = itemName.replace(/([A-Z])([A-Z])/g, '$1 $2');
                                                                    
                                                                    return(
                                                                        <Col key={`key${item.name}`} sm={4} className="item nopadding">
                                                                            <div className="input-container">
                                                                                <label className="item-labels" htmlFor={item.name}>{itemName}</label>
                                                                                <div className="input-wrapper term">
                                                                                    <input 
                                                                                    id={item.name} 
                                                                                    group={group}
                                                                                    applicability={item.applicability}
                                                                                    title={`Optional Choice`} 
                                                                                    placeholder={`...`}
                                                                                    value={this.state.nonRequiredFields[item.name]}
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
                                                    </div>
                                                    {/* 
                                                        <Term
                                                        className="item"
                                                        group={group.group}
                                                        groupLabel={group.Items[0].group}
                                                        items={group.Items}                                       
                                                        nonRequiredFields={this.state.nonRequiredFields}
                                                        action={e=>this.onGroupUpdate(e)}
                                                        fields={this.state.fields}
                                                        key={`item${index}`}/> 
                                                    */}
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
