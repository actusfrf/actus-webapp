import React, { PureComponent } from 'react';
import { Col, Grid, Row } from 'react-bootstrap';
import { Term } from '../Term';
import axios from 'axios';
import DatePicker from "react-datepicker"; 

import "react-datepicker/dist/react-datepicker.css";
import './Form.css';

export class Form extends PureComponent {
  state = {
    groups:[],
    error: false,
    startDate: new Date()
  }

  assemble(a, b){
    a.Group[b.Name] = b.Name;
    return a;
  }

  handleChange(date) {
    console.log(date);
    console.log(this.state);
    this.setState({
      startDate: date
    });
  }

  componentDidMount(){
    let {match} = this.props;
    axios.get(`/data/form_${match.params.id}.json`)
    .then(res => {
      //console.log(res.data);
      if(!res.data.Terms){
        return false;
      }
      let groupToValues = res.data.Terms.reduce(function (obj, item) {
        let gName = item.Group.split(' ').join('');
        obj[gName] = obj[item.Group] || [];
        obj[gName].push(item);
        return obj;
      }, {});

      let groups = Object.keys(groupToValues).map(function (key) {
          return {group: key, Items: groupToValues[key]};
      });
      this.setState({
        groups: groups,
        groupDescription: res.data.Description,
        contractType: res.data.ContractType,
        identifier: res.data.Identifier,
        version: res.data.Version,
        error: {...this.state.error}
      });
    })
    .catch(error => {
      console.log('>>>>>>>>>>> error:', error.response);
    });
  }

  render() {
    let { groups, groupDescription, contractType, identifier, version} = this.state;
    //let { match } = this.props;
    return (
      <div id="form-container" identifier={identifier} version={version}>
        <Grid fluid>
          <Row>
            <Col sm={12} className="contract-main-wrapper">
                <span className="contract-title">{contractType}:</span> <span className="contract-description">{groupDescription}</span>
            </Col>
          </Row>
          <Row>
            <Col sm={4} className="required choices">
              <div className="">All fields below are mandatory to fill in:</div>
            </Col>
            <Col sm={8} className="optional choices">
              <div className="term-group-header">Below are your Optional choices</div>
              {/* <div>
                <DatePicker selected={this.state.startDate} onChange={this.handleChange.bind(this)}  className="item-fields" accept />
              </div> */}
              {
                groups.map((group, groupId) => {
                  //console.log(group);
                  return (
                    <div key={`term_wrapper${groupId}`} className="term-wrapper">
                      <Term className="item" groupName={group.group} groupLabel={group.Items[0].Group} items={group.Items} key={`item${groupId}`}/>
                    </div>
                  )
                })
              }
            </Col>
          </Row>
        </Grid>
        
      </div>
    );
  }
}
