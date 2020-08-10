import React, { PureComponent } from 'react';
import { Contract } from '../Contract';
import { Grid, Row } from 'react-bootstrap';
import axios from 'axios';
import './Landing.css';

export class Landing extends PureComponent {
    state = {
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
                            Object.keys(contracts).map( (key) =>                    
                                <Contract key={key} 
                                contractType={contracts[key].acronym} 
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
