import React, { PureComponent } from 'react';
import { Contract } from '../Contract';
import { Grid, Row } from 'react-bootstrap';
import axios from 'axios';
import './Landing.css';

export class Landing extends PureComponent {
    state = {
	    host: "http://localhost:8080", //"http://marbella.myftp.org:8080", //"http://localhost:8080",
        contracts:[]
    }

    componentDidMount(){
        axios.get(`/data/actus-dictionary.json`)
        .then(res => {
            console.log(res.data);
            this.setState({
                contracts: res.data.taxonomy
            })

        });
        /* axios.get(`${this.state.host}/forms`)
            .then(res => {
                console.log(res.data);
                this.setState({
                    contracts: res.data
                })
        });*/
    }

    render() {
        let contracts = this.state.contracts
        return (
		<div>
		
		{/* intro text */}
	    <div className="section-intro">Choose a Contract Type from the list below in order to define and evaluate specific financial contracts:</div>
            
        {/* contract grid */}
        {console.log("hello")}
                <Grid className="contract-grid">
                    <Row>
                        {
                            /*this.state.contracts.map((contract) =>  
                            <Contract key={contract.contractType} contractType={contract.contractType} 
                                name={contract.name}
                                description={contract.description} />
                            )*/
                            Object.keys(contracts).map( (key) =>                    
                                <Contract key={key} 
                                contractType={contracts[key].accronym} 
                                name={contracts[key].name}
                                description={contracts[key].description} />
                            )
                        }
                    </Row>
                </Grid>
            </div>
        );
    }
}
