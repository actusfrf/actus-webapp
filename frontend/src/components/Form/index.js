import React, { PureComponent } from 'react';
import axios from 'axios';

export class Form extends PureComponent {
  state ={
    fields:[]
  }
  componentDidMount(){
    axios.get('http://localhost:8080/terms')
    .then(res => {
      console.log(res.data);
    });
  }

  render() {
    let { match } = this.props;
    let id = match.params.id.toUpperCase();

    return (
      <div>
        FORM {id}
      </div>
    );
  }
}
