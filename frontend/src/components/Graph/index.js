import * as React from "react";
import * as d3 from "d3";
import { select as d3Select } from 'd3-selection';
import SVGWithMargin from '../SVGWithMargin';

export class Graph extends React.Component {
  constructor(props){
        super(props);
        this.state = {
            data:[props.results],
            width:900,
						height:600,
						margin:50,
            header:'HEADER',
        }
  }

    componentWillReceiveProps(props){
        this.setState({
            data: props.results
        })
    }

  render() {

    // extract states
    let { data, width, height, margin } = this.state;
    console.log(">>>>>>>>>>",data);

    // convert strings to dates
    data.forEach(function(d){d.time = new Date(d.time)});
    console.log(">>>>>>>>>>", data);

    // create axes scales
    let xScale = d3
      .scaleTime()
      .domain(d3.extent(data, d=>d.time))
      .range([0, width - 2*margin]);

    let yScale = d3
      .scaleLinear()
      .domain([0, d3.max(data, d => Math.abs(d.nominalValue))])
      .range([height - 2*margin, 0])
			.nice();

    // create axis objects
    var xAxis = d3.axisBottom(xScale).tickFormat(d3.timeFormat("%Y/%m/%d"));
    var yAxis = d3.axisLeft(yScale);

    // create grid
    // extract tick values	
    var ticks = yAxis.scale().ticks();
    console.log("---------->" + ticks);

    // define grid line type	
    var gridLine = d3
	.line()
	.x(d => xScale(d.time))
	.y(d => yScale(d.value))
	.curve(d3.curveLinear);

    // create grid lines
    const gridLines = ticks.map(function(d) {
	let raw=[{time:xScale.domain()[0],value:d},{time:xScale.domain()[1],value:d}];
	console.log(d + " -- " + raw[1].time);

	return (
		<path
			key={'gridline' + d}
			d={gridLine(raw)}
			className='line'
			style={{fill:'none',strokeWidth: 0.5, stroke: "grey", 
			strokeDasharray: "3,3"}}
		/>
		)
    });

    // create the nominal value state line
    var nominalLine = d3
	.line()
	.x(d => xScale(d.time))
	.y(d => yScale(Math.abs(d.nominalValue)))
	.curve(d3.curveStepAfter);

    // create event arrows
    // define event color mapping
    var col = function(type) {
	switch(type) {
		case "AD":
			return("white");
		case "IED":
			return("red");
		case "PR":
			return("red");
		case "IP":
			return("green");
		case "IPCI":
			return("white");
		case "RR":
			return("white");
		default:
			return("black");
	}
    };

    // define event size mapping
    var size = function(type) {
	switch(type) {
		case "AD":
			return(0);
		case "IED":
			return(2);
		case "PR":
			return(2);
		case "IP":
			return(5);
		case "IPCI":
			return(0);
		case "RR":
			return(0);
		default:
			return("black");
	}
    };

    // define event line type
    var eventLine = d3
	.line()
	.x(d => xScale(d.time))
	.y(d => yScale(d.payoff))
	.curve(d3.curveLinear);

    // create event lines
    let lastPayoff = 0;
    let counter = [];	
    const eventLines = data.map(function(d) {
	console.log(">>>>",col(d.type), d.payoff);
	let raw;
	if(d.type === "PR"){
		raw = [{time:d.time, payoff:Math.abs(d.payoff)+lastPayoff}, {time:d.time, payoff:lastPayoff}];
	}else{
		raw = [{time:d.time, payoff:Math.abs(d.payoff)}, {time:d.time, payoff:0}];
		lastPayoff = Math.abs(d.payoff);
	}
	return(
		<path
			key={'line' + d.time + d.type}
			d={eventLine(raw)}
			className='line'
			style={{fill:'none',strokeWidth: 2, stroke: col(d.type)}}
			markerEnd={col(d.type) === "green"?'url(#arrow2)':'url(#arrow)'}
		/>
		)
    });

    console.log(counter);
    
    // create accrued state lines
    let lastTime = 0;
    let lastAccrued = 0;
    const accruedLines = data.map(
	function(d){
		let raw;	
		if(col(d.type) === 'green'){
			raw = [{time:d.time, payoff:Math.abs(d.payoff)}, {time:lastTime, payoff:lastAccrued}];
		}else{
			raw = [{time:d.time, payoff:Math.abs(d.nominalAccrued)}, {time:lastTime, payoff:lastAccrued}];
		}
		lastTime = d.time;
		lastAccrued = Math.abs(d.nominalAccrued);
		return(
			<path
				key={'line' + d.time + d.type}
				d={eventLine(raw)}
				className='line'
				style={{fill:'none',strokeWidth: 2, stroke: "green", strokeDasharray: "3,3"}}
			/>
			)
	})

    // create event labels
    const eventLabels = data.map(function(d) {
	if(d.type === 'IPCI'){
		return(
			<text key={'label' + d.time + d.type} 
			x={xScale(d.time)} y={yScale(Math.abs(d.nominalValue))} 
			fontSize="18px">
			{d.type} </text>)
	}else{
		return(
			<text key={'label' + d.time + d.type} 
			x={xScale(d.time)} y={yScale(Math.abs(d.payoff))} 
			fontSize="18px">
			{d.type} </text>)
	}
    })
    console.log("000000 " + eventLines);


    return (
      <div>
	<SVGWithMargin className="container" height={height} margin={margin} width={width}>
		<g id={"group"}>
			{/* <defs>
				<marker id="arrow" viewBox="0 0 10 10"
							refX="5" refY="5" 
							markerUnits="strokeWidth"
							markerWidth="5" markerHeight="5"
							orient="auto">
				</marker>
			</defs> */}
			<defs>
				<marker viewBox="0 0 10 10" id="arrow" markerWidth="5" markerHeight="5" refX="5" refY="5" orient="0deg" markerUnits="strokeWidth">
					<path d="M0,0 L5,5 L10,0" style={{fill:'none', strokeWidth:2, stroke:'red'}}  />
				</marker>
				<marker viewBox="0 0 10 10" id="arrow2" markerWidth="5" markerHeight="5" refX="5" refY="5" orient="0deg" markerUnits="strokeWidth">
					<path d="M0,0 L5,5 L10,0" style={{fill:'none', strokeWidth:2, stroke:'green'}}  />
				</marker>
			</defs>
			{/* draw axes*/}
			<g className="xAxis" ref={node => d3Select(node).call(xAxis)} style={{ transform: `translateY(${height-2*margin}px)`, }} />
			<g className="yAxis" ref={node => d3Select(node).call(yAxis)} /></g>
		{/* draw grid lines */}
		<g transform={"translateY(${height-2*margin}px)"}>
			{gridLines}
		</g>
		{/* draw nominal value line */}
		<path id={"line"} d={nominalLine(data)} stroke="red" strokeWidth="2" strokeDasharray="3,3" fill="none"/>
		
		{/* draw event lines */}
		<g transform={"translateY(${height-2*margin}px)"}>
			{eventLines}
		</g>
		<g transform={"translateY(${height-2*margin}px)"}>
			{accruedLines}
		</g>
		{/* draw event labels */}
		<g transform={"translateY(${height-2*margin}px)"}>
			{eventLabels}
		</g>
        </SVGWithMargin>
      </div>
    );
  }
}
