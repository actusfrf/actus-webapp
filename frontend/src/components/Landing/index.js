import React, { PureComponent } from 'react';
import { Contract } from '../Contract';
import { Grid, Row } from 'react-bootstrap';
import axios from 'axios';
import './Landing.css';

export class Landing extends PureComponent {
    state = {
	    host: "http://marbella.myftp.org:8080", //"http://localhost:8080",
        contracts:[]
    }

    componentDidMount(){
        //axios.get(`/data/forms.json`)
        axios.get(`${this.state.host}/forms`)
            .then(res => {
                console.log(res.data);
                this.setState({
                    contracts: res.data
                })
        });
    }

    render() {
        return (
		<div>
		
		{/* intro text */}
	    <div className="section-intro">Choose a Contract Type from the list below in order to define and evaluate specific financial contracts:</div>
            
        {/* contract grid */}
        {console.log(this.state.contracts)}
                <Grid className="contract-grid">
                    <Row>
                        {
                            this.state.contracts.map((contract) =>                         
                                <Contract key={contract.contractType} contractType={contract.contractType} 
                                name={contract.name}
                                description={contract.description} />
                            )
                        }
                    </Row>
                </Grid>
            </div>
        );
    }
}
