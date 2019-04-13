import React, { PureComponent } from 'react';
import './Footer.css';

export default class Footer extends PureComponent { 
    render(){
        return (
        	<div className="footer-wrapper">
			<p>&copy; 2019 - present <a href="https://actusfrf.org" target="_blank"> ACTUS Financial Research Foundation </a> </p>
		</div>
        )
    }
}
