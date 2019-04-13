import React, { PureComponent } from 'react';
import './Header.css';

export default class Header extends PureComponent { 
    render(){
        return (
		<div className="header-wrapper">
			<a href="https://actusfrf.org" target="_blank">
	                	<img src="https://raw.githubusercontent.com/actusfrf/actus-resources/master/logos/actus_logo.jpg" width="200" alt="ACTUS Logo"/>
                	</a>
		</div>
        )
    }
}
