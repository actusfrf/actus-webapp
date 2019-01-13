import React, { PureComponent } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import ToolTip from '../ToolTip';
import './Term.css';

export class Term extends PureComponent {
    render() {
        let { items, groupName, groupLabel, fields } = this.props;

        return ( 
            <div id={groupName} className="items-group">
                <div className="item-header">{groupLabel}</div>
                <Grid fluid>
                    <Row>
                    {
                    items.map(item => {
                            let itemName = item.Name;
                            let itemValue = fields[itemName];
                            itemName = itemName.replace(/([a-z])([A-Z])/g, '$1 $2');
                            itemName = itemName.replace(/([A-Z])([A-Z])/g, '$1 $2');
                            
                            return(
                                <Col key={`key${item.Name}`} sm={4} className="item nopadding">
                                    <div className="input-container">
                                        <label className="item-labels" htmlFor={item.Name}>{itemName}</label>
                                        <div className="input-wrapper">
                                            <input id={item.Name} title={`Optional Choice`} placeholder={itemName} value={itemValue}  onChange={()=>{}} className="item-fields" type="text" />
                                            <ToolTip description={item.Description} />
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