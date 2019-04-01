import React, {PureComponent} from 'react';
import {Col, Grid, Row} from 'react-bootstrap';
//import axios from 'axios';
import './Results.css';
import { Graph } from '../Graph';

export class Results extends PureComponent {
    state = {
        results: [],
        currentTab: 'Table',        
    }

    componentDidMount() { 
        let response = [...this.props.location.state.data];
        this.setState({
            results: response
        })  
    }

    render() {        
        let { results, currentTab } = this.state;
        return (
            <div id="results-container" identifier="" version="">
                <Grid fluid>
                    <Row>
                        <Col sm={12} className="results-main-wrapper">
                            <span className="results-title">RESULT:</span>
                            <span className="results-description">group description</span>
                        </Col>
                    </Row>
                    <Row>
                        <Col sm={12} className="results-graph-wrapper">
                            <Graph data={results} />
                        </Col>
                    </Row>
                    {currentTab === 'Table' && 
                    <Row>
                        <Col sm={12}>
                            <Grid fluid className="results-table-wrapper">
                                <Row className="table-header-container">
                                    <Col sm={1} className="table-header">Type</Col>
                                    <Col sm={2} className="table-header">Time</Col>
                                    <Col sm={2} className="table-header">Payoff</Col>
                                    <Col sm={1} className="table-header">Currency</Col>
                                    <Col sm={2} className="table-header">Nominal Value</Col>
                                    <Col sm={2} className="table-header">Nominal Rate</Col>
                                    <Col sm={2} className="table-header">Nominal Accrued</Col>
                                </Row>
                            {
                                results.map((result, index) => {
                                    console.log(result);
                                    return (
                                        <Row key={index}>
                                            <Col className="cell-content" sm={1}>{result.type}</Col>
                                            <Col className="cell-content" sm={2}>{result.time}</Col>
                                            <Col className="cell-content" sm={2}>{result.payoff}</Col>
                                            <Col className="cell-content" sm={1}>{result.currency}</Col>
                                            <Col className="cell-content" sm={2}>{result.nominalValue}</Col>
                                            <Col className="cell-content" sm={2}>{result.nominalRate}</Col>
                                            <Col className="cell-content" sm={2}>{result.nominalAccrued}</Col>                                            
                                        </Row>
                                    )
                                })
                            }
                            </Grid>
                        </Col>
                    </Row>
                    }
                </Grid>
            </div>          
        )
    }
}