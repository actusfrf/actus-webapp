import React, { PureComponent } from 'react';
import { Link } from 'react-router-dom'
import { Col } from 'react-bootstrap';
import './Contract.css'

export class Contract extends PureComponent {
	
	constructor(props){
     		super(props);
        	this.state={
        		contractType:"",
        		name:"",
				description:""
			};
	}

	componentDidMount(){
	        this.setState({
				contractType: this.props.contractType,
        		name:this.props.name,
				description:this.props.description
			});
	}

    render(){
	let contract = this.state;
	console.log(contract)
	  return (
		<Col sm={12} md={6} className="contract-wrapper">
			<div className="contract-content">
		        	<Link className="contract-link" to={`/form/${contract.contractType}`}>{contract.contractType}</Link>                
				<div className="contract-name">{contract.name}</div>
				<div className="contract-description">{contract.description}</div>
			</div>
		</Col>
		);
	}
}

