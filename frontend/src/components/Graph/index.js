import React, {PureComponent} from 'react';
import './Graph.css';

export class Graph extends PureComponent {
    state = {
        data:[]
    }

    componentDidMount() {
        this.setState({
            data:[...this.props.data]
        });

        //TODO: get top nominal value for vertical values
        //Increment by 250
        //TODO: get event type for horizontal values
        //distribute horizontally by percent 100% / total length of event types
    }

    render() {
        return (
            <div>GRAPH HERE</div>
        )
    }
}