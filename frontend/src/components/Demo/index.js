import React, { PureComponent } from 'react';
import { Col }  from 'react-bootstrap';
import './Demo.css';

export default class Demo extends PureComponent { 
    state = {
        fields:[],
        label:'',
        identifier:'',
        terms:{},
        demoId: '',
        riskFactorData: [],
        passDemoData: null
    }

    passDemoData(){        
        this.state.passDemoData(this.state.terms, this.state.riskFactorData, this.state.demoId)
    }

    componentDidMount() {
        this.setState({
            terms: this.props.terms,
            identifier: this.props.identifier,
            demoId: this.props.demoId,
            label: this.props.label,
            riskFactorData: this.props.riskFactorData,
            passDemoData: this.props.passDemoData
        })
    }

    render(){
        let {description, index} = this.props;
        return (
            <Col sm={4} className="demo-col">
                <div className="demo-item">
                    <div className="demo-item-content">
                        <h4 onClick={e => this.passDemoData()} className="demo-case-title">Case {index}</h4>
                        <article className="demo-text">{(description !== null)?description:"No Description"}</article>
                    </div>
                </div>
            </Col>
        )
    }
}