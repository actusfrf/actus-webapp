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
                {
                    contracts.map((contract, index) => {
                        return (
                            <Contract key={`_contract_${index}`} type={contract.type} contracts={contract.items} />
                        )
                    })
                }
            </div>
        );
    }
}
