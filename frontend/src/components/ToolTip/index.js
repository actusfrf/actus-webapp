import React, { PureComponent } from 'react';
import './ToolTip.css';
import helperIcon from './help.jpg';

export default class ToolTip extends PureComponent { 
    
    render(){
        let {description} = this.props;
        return (
            <i className="tool-tip">
                <img src={helperIcon} alt="Information"/>
                <div className="tool-tip-copy">{description}</div>
            </i>
        )
    }
}