import React, { PureComponent } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import ToolTip from '../ToolTip';
import './Term.css';

export class Term extends PureComponent {
   
    render() {
        let { items, groupName, groupLabel, fields } = this.props;  
               
        if(items.length > 1){
            return ( 
                <div id={groupName} className="items-group">
                    <div className="item-header">{groupLabel}</div>
                    <Grid fluid>
                        <Row>
                        {
                        items.map(item => {
                                let itemName = item.name;
                                let itemValue = fields[itemName];

                                itemName = itemName.replace(/([a-z])([A-Z])/g, '$1 $2');
                                itemName = itemName.replace(/([A-Z])([A-Z])/g, '$1 $2');

                                return(
                                    <Col key={`key${item.name}`} sm={4} className="item nopadding">
                                        <div className="input-container">
                                            <label className="item-labels" htmlFor={item.name}>{itemName}</label>
                                            <div className="input-wrapper">
                                                <input id={item.name} 
                                                title={`Optional Choice`} 
                                                placeholder={itemName} 
                                                onChange={()=>{}}
                                                className="item-fields" 
                                                type="text" />
                                                <ToolTip description={item.description} />
                                            </div>                                        
                                        </div>
                                    </Col>
                                )
                            })
                        }
                        </Row>
                    </Grid>
                </div>
            );
        }   
    }
}