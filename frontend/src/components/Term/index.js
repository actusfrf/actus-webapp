import React, { PureComponent } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import ToolTip from '../ToolTip';
import './Term.css';

export class Term extends PureComponent {
    render() {
        let { items, groupName, groupLabel } = this.props;
        console.log('jellyfish:', this.props);
        return ( 
            <div id={groupName} className="items-group">
                <div className="item-header">{groupLabel}</div>
                <Grid fluid>
                    <Row>
                    {
                    items.map(item => {
                            let itemName = item.Name;
                            itemName = itemName.replace(/([a-z])([A-Z])/g, '$1 $2');
                            itemName = itemName.replace(/([A-Z])([A-Z])/g, '$1 $2');
                            
                            return(
                                <Col key={`key${item.Name}`} sm={4} className="item nopadding">
                                    <div className="input-container">
                                        <label className="item-labels" htmlFor={item.Name}>{itemName}</label>
                                        <div className="input-wrapper">
                                            <input id={item.Name} title={item.Description} placeholder={itemName} className="item-fields" type="text" />
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