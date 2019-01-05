import React, { PureComponent } from 'react';
import './Term.css';

export class Term extends PureComponent {
    render() {
        let { items, groupName, groupLabel } = this.props;
        //console.log('jellyfish:', this.props);
        return ( 
            <div id={groupName} className="items-group">
                <div className="item-label">{groupLabel}</div>
                {
                    items.map(item => {
                        return(
                            <div key={`key${item.Name}`} className="item">{item.Name}</div>
                        )
                    })
                }
            </div>
        );
    }
}