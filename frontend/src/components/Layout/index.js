import React, { Component } from 'react';
import Header from '../Header';
import './Layout.css';

export class Layout extends Component {
  displayName = Layout.name

  render() {
    return (
      <div className="main-container">
        <Header />
        {this.props.children}
      </div>
    );
  }
}
