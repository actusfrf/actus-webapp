import React, {PureComponent} from 'react';
import './Graph.css';

export class Graph extends PureComponent {
    state = {
        data:[],
        sections: 5,
        maxValue: 0,
        stepSize: 0,
        columnSize: 50,
        rowSize: 60,
        margin:0,
        header:'',
    }

    componentDidMount() {
        this.setState({
            data:[...this.props.results]
        });     
        console.log(">>results>>",this.props.results);
        //this.renderGraph();
        //TODO: get top nominal value for vertical values
        //Increment by 250
        //TODO: get event type for horizontal values
        //distribute horizontally by percent 100% / total length of event types
    }

    renderGraph() {
        let {maxValue, stepSize, columnSize, rowSize, margin, header, sections} = this.state;
        const canvas = this.refs.canvas;
        const ctx = this.refs.canvas.getContext('2d');
        ctx.clearRect(0,0,canvas.width, canvas.height);
        ctx.fillStyle = '#000';

        let yScale = (canvas.height - columnSize - margin) / maxValue;
        let xScale = (canvas.width - rowSize) / (sections + 1);
        let count =  0;
        let scale = 0;

        ctx.strokeStyle="#000;"; // background black lines
        ctx.beginPath();
        ctx.font = "25 pt Arial;"
        ctx.fillText(header, margin, columnSize - margin);
        ctx.font = "16 pt Helvetica";
        console.log("maxValue",maxValue);

        //Draw background lines and values
        // for (scale = maxValue; scale >= 0 ; scale = scale - stepSize) {
        //     let y = columnSize + (yScale * count * stepSize); 
        //     ctx.fillText(scale, margin, y + margin - 16);
        //     ctx.moveTo(rowSize,y)
        //     ctx.lineTo(canvas.width-margin,y)
        //     count++;
        // }
        // ctx.stroke();
    }

    getHeight(value, yScale, canvas){
        return canvas.height - value * yScale ;
    }

    render() {
        return (
            <div>
                <div className="text-center">RESULTS</div>
                <canvas ref='canvas' id="GraphRender" className="graph-render" width="680" height="480" />
            </div>
        )
    }
}