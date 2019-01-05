import React, { PureComponent } from 'react';
import { Link } from 'react-router-dom'
import { Col, Grid, Row } from 'react-bootstrap';
import './Contract.css';

export class Contract extends PureComponent { 
    render(){
        let {type, contracts} = this.props;
        return (
            <div className="">
                <div className="contract-title">{type}</div>
                <Grid className="contract-grid">
                    <Row>
                        {
                            contracts.map((contract, index) =>                         
                                <Col key={`contract_col_${index}`} sm={12} md={3} className="contract-wrapper">
                                    <div className="contract-content">
                                        <Link className="contract-link" to={`/form/${contract.name}`}>{contract.name}</Link>
                                        <div>{contract.description}</div>
                                    </div>
                                </Col>
                            )
                        }
                    </Row>
                </Grid>
            </div>
            
        )
    }
}