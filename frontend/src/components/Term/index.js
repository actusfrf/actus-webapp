import React, { PureComponent } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import './Term.css';

export class Term extends PureComponent {
    render() {
        let { items, groupName, groupLabel } = this.props;
        console.log('jellyfish:', this.props);
        return ( 
            <div id={groupName} className="items-group">
                <div className="item-label">{groupLabel}</div>
                <Grid fluid>
                    <Row>
                    {
                    items.map(item => {
                            return(
                                <Col key={`key${item.Name}`} sm={3} className="item nopadding">
                                    <div className="input-container">
                                        <input id={item.Name} title={item.Description} placeholder={`ttt`} className="item-fields" type="text" />
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