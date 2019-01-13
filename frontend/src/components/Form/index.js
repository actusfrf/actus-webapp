import React, {PureComponent} from 'react';
import {Col, Grid, Row} from 'react-bootstrap';
import {Term} from '../Term';
import axios from 'axios';

/* eslint-disable */
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import './Form.css';

export class Form extends PureComponent {
    state = {
        groups: [],
        error: false,
        fields: {},
        startDate: new Date()
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
        const data = JSON.stringify(this.state.fields);
        console.log(this.state.fields);

        /*fetch('/api/endpoint.js',{
          method: 'POST',
          body: JSON.stringify(data)
        })*/
    }

    componentDidMount() {
        let {match} = this.props;
        axios
            .get(`/data/form_${match.params.id}.json`)
            .then(res => {
                if (!res.data.Terms || !res || !res.data) {
                    return false;
                }

                let groupToValues = res
                    .data
                    .Terms
                    .reduce(function (obj, item) {
                        let gName = item
                            .Group
                            .split(' ')
                            .join('');
                        obj[gName] = obj[item.Group] || [];
                        obj[gName].push(item);
                        return obj;
                    }, {});

                let fields = {};

                //auto populate fields with values for testing
                res
                    .data
                    .Terms
                    .map(function (term, index) {
                        fields[term.Name] = {};
                        fields[term.Name] = `LoremIpsum ${term.Name} ${index}`;
                    });

                let groups = Object
                    .keys(groupToValues)
                    .map(function (key) {
                        return {group: key, Items: groupToValues[key]};
                    });

                this.setState({
                    groups: groups,
                    fields: {
                        ...fields
                    },
                    totalFields: Object
                        .keys(fields)
                        .length,
                    groupDescription: res.data.Description,
                    contractType: res.data.ContractType,
                    identifier: res.data.Identifier,
                    version: res.data.Version,
                    error: {
                        ...this.state.error
                    }
                });
            })
            .catch(error => {
                console.log('>>>>>>>>>>> error:', error);
            });
    }

    render() {
        let {groups, groupDescription, contractType, identifier, version} = this.state;
        //let { match } = this.props;
        return (
            <div id="form-container" identifier={identifier} version={version}>
                <Grid fluid>
                    <Row>
                        <Col sm={12} className="contract-main-wrapper">
                            <span className="contract-title">{contractType}:</span>
                            <span className="contract-description">{groupDescription}</span>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={4} className="required choices">
                            <div className="">All fields below are mandatory to fill in:</div>
                        </Col>
                        <Col sm={8} className="optional choices">
                            <div className="term-group-header">Below are your Optional choices</div>
                            {/* <div>
                                    <DatePicker
                                        selected={this.state.startDate}
                                        onChange={this
                                        .handleChange
                                        .bind(this)}
                                        className="item-fields"
                                        accept/>
                                </div> */}
                            {groups.map((group, groupId) => {
                                return (
                                    <div key={`term_wrapper${groupId}`} className="term-wrapper">
                                        <Term
                                            className="item"
                                            groupName={group.group}
                                            groupLabel={group.Items[0].Group}
                                            items={group.Items}
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
                        <Col sm={6} className="text-left">
                            <input type="submit" value="SEND" onClick={(e) => this.handleSubmit(e)}/>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
}
