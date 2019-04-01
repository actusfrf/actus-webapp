import React, { PureComponent } from 'react';
import { Redirect } from 'react-router-dom'
import { Col, Grid, Row}  from 'react-bootstrap';
import axios from 'axios';
import './Demo.css';

export default class Demo extends PureComponent { 
    state = {
        fields:[],
        label:'',
        identifier:'',
        terms:{},
        demoId: '',
        passDemoData: null
    }

    passDemoData(){
        //console.log(this.state.terms);
        this.state.passDemoData(this.state.terms, this.state.demoId)
    }

    componentDidMount() {
        this.setState({
            terms: this.props.terms,
            identifier: this.props.identifier,
            demoId: this.props.demoId,
            label: this.props.label,
            passDemoData: this.props.passDemoData
        })
    }

    render(){
        let {description, index} = this.props;
        return (
            <Col sm={4} className="demo-col">
                <div className="demo-item">
                    <div className="demo-item-content">
                        <h4 onClick={e => this.passDemoData()} className="demo-case-title">Case {index+1}</h4>
                        <article className="demo-text">{(description !== null)?description:"No Description"}</article>
                    </div>
                </div>
            </Col>
        )
    }
}