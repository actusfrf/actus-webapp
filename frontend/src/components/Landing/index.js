import React, { PureComponent } from 'react';
import { Contract } from '../Contract';
import { Grid, Row } from 'react-bootstrap';
import axios from 'axios';
import './Landing.css';

export class Landing extends PureComponent {
    state = {
        contractDetails:[],
        contractIdentifiers:[]
    }

    componentDidMount(){
        axios.get(`/data/actus-dictionary.json`)
        .then(res => {
            this.setState({
                contractDetails: res.data.taxonomy
            })
        });
        axios.get(`/data/covered-contracts.json`)
        .then(res => {
            this.setState({
                contractIdentifiers: res.data.contracts
            })
        });
    }

    render() {
        let contractDetails = this.state.contractDetails
        let contractIdentifiers = this.state.contractIdentifiers
        return (
		<div>
		
		{/* intro text */}
	    <div className="section-intro">Choose a Contract Type from the list below in order to define and evaluate specific financial contracts:</div>
            
        {/* contract grid */}
                <Grid className="contract-grid">
                    <Row>
                        {
                            Object.keys(contractDetails).map( (key) => 
                                {
                                    if(contractIdentifiers.indexOf(key) != -1) {
                                        return(                
                                            <Contract key={key} 
                                            contractType={contractDetails[key].acronym} 
                                            name={contractDetails[key].name}
                                            description={contractDetails[key].description} />
                                        )}
                                }
                            )
                        }
                    </Row>
                </Grid>
            </div>
        );
    }
}
