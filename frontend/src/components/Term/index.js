import React, { PureComponent } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import ToolTip from '../ToolTip';
import './Term.css';

export class Term extends PureComponent {
    render() {
        var { items, group, groupLabel, action} = this.props;  
               
        if(items.length > 1){
            return ( 
                <div id={group} className="items-group">
                    <div className="item-header">{groupLabel}</div>
                    <Grid fluid>
                        <Row>
                        {
                        items.map((item, index) => {
                                let itemName = item.name;
                                let group = item.group;
                                itemName = itemName.replace(/([a-z])([A-Z])/g, '$1 $2');
                                itemName = itemName.replace(/([A-Z])([A-Z])/g, '$1 $2');
                                
                                return(
                                    <Col key={`key${item.name}`} sm={4} className="item nopadding">
                                        <div className="input-container">
                                            <label className="item-labels" htmlFor={item.name}>{itemName}</label>
                                            <div className="input-wrapper term">
                                                {console.log(group)}
                                                <input 
                                                id={item.name} 
                                                group={group}
                                                applicability={item.applicability}
                                                title={`Optional Choice`} 
                                                placeholder={`...`}
                                                onKeyUp={e=> action(e)}
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
        } else{
            return null;
        }  
    }
}