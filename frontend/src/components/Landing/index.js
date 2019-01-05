import React, { PureComponent } from 'react';
import { Contract } from '../Contract';
import axios from 'axios';
import './Landing.css';

export class Landing extends PureComponent {
    state = {
        contracts:[]
    }

    componentDidMount(){
        axios.get(`/data/forms.json`)
        .then(res => {
            console.log(res.data);
            this.setState({
                contracts: res.data.contracts
            })
        });
    }

    render() {  
        let {contracts} = this.state;  
        return (
            <div>
                <div className="section-title">Contract Calculator Manual</div>
                <div className="section-description">
                    <a href="http://projectactus.org/ACTUS/data/CT_Calculator_Manual.odt" rel="noopener noreferrer" target="_blank">Click Here</a> to access Contract Calculator Manual
                </div>
                {
                    contracts.map((contract, index) => {
                        console.log(contract);
                        return (
                            <Contract key={`_contract_${index}`} type={contract.type} contracts={contract.items} />
                        )
                    })
                }
            </div>
        );
    }
}
