import React, {PureComponent} from 'react';
import {Col, Grid, Row} from 'react-bootstrap';
import {Term} from '../Term';
import axios from 'axios';

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
        }
    }

    assemble(a, b) {
        a.Group[b.Name] = b.Name;
        return a;
    }

    handleChange(date) {
        console.log(date);
        console.log(this.state);
        this.setState({startDate: date});
    }

    handleReset(e) {
        console.log('Reset');
    }

    handleSubmit(e) {
        e.preventDefault();
        let allAnswers = Object.assign({},this.state.requiredFields, this.state.nonRequiredFields);
        console.log(allAnswers);

        let tf = this.state.testFields;
        let config = {
            'mode': 'cors',
            'headers': {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
            },
            'withCredentials': true,
            'credentials': 'omit'
        }
        axios.post('http://localhost/events',tf, config)
            .then(res => {
                console.log(res);
                console.log(res.data);
            });

        // this.postData(`http://localhost/events`,tf)
        //     .then(data => console.log(JSON.stringify(data)))
        //     .catch(error => console.error(error));
    }
    // postData(url = ``, data = {}) {
    // // Default options are marked with *
    //     return fetch(url, {
    //         method: "POST", // *GET, POST, PUT, DELETE, etc.
    //         mode: "no-cors", // no-cors, cors, *same-origin
    //         cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    //         credentials: "omit", // include, *same-origin, omit
    //         headers: {
    //             "Content-Type": "application/json",
    //             // "Content-Type": "application/x-www-form-urlencoded",
    //         },
    //         redirect: "follow", // manual, *follow, error
    //         referrer: "no-referrer", // no-referrer, *client
    //         body: JSON.stringify(data), // body data type must match "Content-Type" header
    //     })
    //     .then(response => response.json()); // parses response to JSON
    // }

    validateFields(){
        let fields = this.state.requiredFields;
        let errorCount = 0;
        for(var f in fields){
            if(fields[f] === ''){
                errorCount++;
            }
        }
        //console.log("error Count:", errorCount);

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
        this.setState({
            nonRequiredFields:{
                ...this.state.nonRequiredFields,
                [e.target.id]: e.target.value
            }
        });
    }
    
    componentDidMount() {
        console.log('did mount');
        let {match} = this.props;
        axios
            .get(`http://localhost/terms/meta/${match.params.id}`)
            .then(res => {
                if (!res.data[0].terms || !res || !res.data) {
                    return false;
                }
                let responseData = res.data[0];

                let optionalFields = res.data[0].terms.filter(n => (n.applicability.indexOf('NN') <= -1));
                let mandatoryFields = res.data[0].terms.filter(n => (n.applicability.indexOf('NN') > -1));

                let requiredFields = Object.assign({}, ...mandatoryFields.map(o=>({[o.name]: ''})));
                let nonRequiredFields = Object.assign({}, ...optionalFields.map(o=>({[o.name]: ''})));
                //console.log(requiredFields);

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

                this.setState({
                    groups: groups,
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

    render() {
        let {groups, groupDescription, contractType, identifier, version, mandatoryFields} = this.state;
        //let { match } = this.props;
        return (
            <div id="form-container" identifier={identifier} version={version}>
                <Grid fluid>
                    <Row>
                    <Col sm={12} className="contract-main-wrapper">
                    <span className="contract-title">Demo Case</span>
                    </Col>
                        <Col sm={12} className="contract-main-wrapper">
                            <span className="contract-title">{contractType}:</span>
                            <span className="contract-description">{groupDescription}</span>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={5} className="required choices">
                            <div className="term-group-header">All fields below are mandatory to fill in:</div>
                            <div className="field-wrapper">
                                <div className="items-group">
                                    <Grid fluid>
                                        <Row>
                                            {
                                                mandatoryFields.map((m, groupId) => {
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
