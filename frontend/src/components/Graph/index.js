import React, {Component} from 'react';
import './Graph.css';

export class Graph extends Component {
    constructor(props){
        super(props);
        this.state = {
            data:[props.results],
            maxValue: 10,
            stepSize: 0,
            columnSize: 50,
            rowSize: 60,
            margin: 20,
            header:'HEADER',
        }
    }

    componentWillReceiveProps(props){
        //this.state.data.setData
        console.log("props", props.results);
        this.renderGraph(props.results);
    }

    renderGraph(results) {
        console.log(">>>>>>>>>>",results);
        let {columnSize, rowSize, margin, header} = this.state;
        
        const canvas = this.refs.canvas;
        const ctx = this.refs.canvas.getContext('2d');
        ctx.clearRect(0,0,canvas.width, canvas.height);
        ctx.fillStyle = '#000';

        let max = Math.ceil(Math.max.apply(Math, results.map(o =>{ return o.payoff})))+1;
        let stepSize = Math.ceil(max / 15);
        let sections = results.length;

        let yScale = (canvas.height - columnSize - margin) / max;
        let xScale = (canvas.width - rowSize) / (sections + 1);
        let scale = max;
        let count =  0;
        let y = 0;

        ctx.strokeStyle="rgba(0,0,0,1);"; // background black lines
        ctx.beginPath();
        ctx.font = "25 pt Arial;"
        ctx.fillText(header, margin, columnSize - margin);
        ctx.font = "16 pt Helvetica";
        ctx.stroke();
        console.log(yScale, xScale, count);

        ctx.moveTo(rowSize+10, canvas.height-margin + 10);
        ctx.lineTo(rowSize+10,margin+16);
        ctx.moveTo(canvas.width - margin - 10, canvas.height-margin + 10);
        ctx.lineTo(canvas.width - margin - 10, margin+10)
        //ctx.strokeStyle="rgba(0,0,0,.5);"

        for (scale = max; scale >= 0; scale = scale - stepSize) {
            y = columnSize + (yScale * count * stepSize);
            ctx.fillText(scale, margin, y + margin - 16);
            ctx.moveTo(rowSize, y)
            ctx.lineTo(canvas.width - margin, y);
            count++;
        }
        ctx.stroke();

        ctx.font = "20 pt Verdana";
        ctx.textBaseline = "bottom";
        ctx.beginPath();
        ctx.lineWidth = 2;
        ctx.moveTo(rowSize + 10, canvas.height - 16);
        for (var i = 0; i < results.length; i++) {
            //computeHeight(itemValue[i]);
            if(results[i].payoff >= 0){

                y = this.getHeight(results[i].payoff, yScale, canvas)
                ctx.fillStyle = "#000";
                ctx.strokeStyle = "#FF0000";
                ctx.lineTo(i===0?rowSize+10:xScale * (i + 1), i===0?canvas.height-16:y - margin);
                
            }
        }
        ctx.stroke();
        
        for (var n = 0; n < results.length; n++) {
            //computeHeight(itemValue[i]);
            if(results[n].payoff >= 0){

                y = this.getHeight(results[n].payoff, yScale, canvas);
                ctx.textAlign = "center";
                ctx.font = "bold 10px Arial";
                ctx.fillStyle = "#000000";
                ctx.fillText(results[n].type.toUpperCase(), n===0?rowSize+10:xScale * (n + 1), y - margin - 15);
                ctx.fillText(results[n].payoff.toFixed(3), n===0?rowSize+10:xScale * (n + 1), y - margin - 4);
                
                ctx.beginPath();
                ctx.lineWidth = 1.1;
                ctx.fillStyle = "#ffcccc";
                ctx.arc(n===0?rowSize+10:xScale * (n + 1), y - margin, 3, 0, 2 * Math.PI, false);
                ctx.fill();
                ctx.stroke();                
            }
        }
    }

    getHeight(value, yScale, canvas){
        return canvas.height - value * yScale ;
    }

    render() {
        return (
            <div>
                <div className="text-center">RESULTS</div>
                <canvas ref='canvas' id="GraphRender" className="graph-render" width="1024" height="723" />
            </div>
        )
    }
}