import React, { PureComponent } from 'react';
import './Header.css';

export default class Header extends PureComponent { 
    render(){
        return (
	<header>
        	<a href="/"><img className="header-img" src ="https://raw.githubusercontent.com/actusfrf/actus-resources/master/logos/actus_logo.jpg" alt="ACTUS logo"/></a>
        	<h1>The ACTUS Demo App</h1>
    	</header>
        )
    }
}
