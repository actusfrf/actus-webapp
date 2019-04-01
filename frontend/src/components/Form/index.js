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
        demos:[],
        showDemos: false,
        showForm: true,
        totalFields: 0,
        groupDescription: "",
        contractType: "",
        identifier: "",
        version: "",
        testFields:{
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
        },
        results:{},
        isFetching: false,
        redirect: false,
        host: "http://localhost", //http://190.141.20.26/
    }

    assemble(a, b) {
        a.Group[b.Name] = b.Name;
        return a;
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
        //let host = "http://190.141.20.26/"; //http://localhost //"http://190.141.20.26/"
        
        let tf = this.state.requiredFields;
        let config = {
            'mode': 'cors',
            'headers': {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
            },
            'withCredentials': true,
            'credentials': 'omit'
        }

        axios.post(this.state.host+'/events',tf)
            .then(res => {
                console.log(res.data);
                this.setState({
                    results: res.data,
                    isFetching: false,
                    redirect: true,
                })
            })
            .catch(error => {
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

    updateField(e) {
        this.setState({
            requiredFields: {
                ...this.state.requiredFields,
                [e.target.id]: e.target.value
            }
        });
        
        this.validateFields();
    }

    onComponentUpdate(e){
        console.log('Did Update');
        this.setState({
            nonRequiredFields:{
                ...this.state.nonRequiredFields,
                [e.target.id]: e.target.value
            }
        });
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
                    totalFields: Object.keys(fields).length,
                    groupDescription: responseData.description,
                    contractType: responseData.contractType,
                    identifier: responseData.identifier,
                    version: responseData.version,
                    error: {
                        ...this.state.error
                    }
                });
                //console.log(this.state);
            })
            .catch(error => {
                console.log('>>>>>>>>>>> error:', error);
            });
    }  

    fetchDemos(id){
        axios
            .get(`${this.state.host}/demos/meta/${id}`)
            .then(res => {
                console.log(res);
                this.setState({
                    demos: res.data
                })
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
        this.setState({
            requiredFields: {
                ...this.state.requiredFields,
                ...terms,
            }
        })
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
                                    {/* DatePicker component do not remove.
                                        <div>
                                            <DatePicker
                                                selected={this.state.startDate}
                                                onChange={this
                                                .handleChange
                                                .bind(this)}
                                                className="item-fields"
                                                accept/>
                                        </div> 
                                        */}
                                    {
                                        groups.map((group, groupId) => {
                                            return (
                                                <div key={`term_wrapper${groupId}`} className="term-wrapper">
                                                    <Term
                                                        className="item"
                                                        groupName={group.group}
                                                        groupLabel={group.Items[0].group}
                                                        items={group.Items}
                                                        action={e => this.onComponentUpdate(e)}
                                                        fields={this.state.fields}
                                                        key={`item${groupId}`}/>
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
