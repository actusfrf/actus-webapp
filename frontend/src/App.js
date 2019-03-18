import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import { Layout } from './components/Layout';
import { Form } from './components/Form';
import { Results } from './components/Results';
import { Landing } from './components/Landing';
import { NotFound } from './components/NotFound';
import './styles/App.css';

class App extends Component {
  render() {
    return (
      <Layout>
          <Switch>
              <Route exact path='/' render={ props => <Landing {...props} />} />
              <Route exact path='/form/:id' render={ props => <Form {...props} />} />
              <Route exact path='/results' render={ props => <Results {...props} />} />
              <Route component={NotFound}/>
          </Switch>
      </Layout>
    );
  }
}
export default App;
