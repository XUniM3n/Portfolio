import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux'
import './index.css';
import {App} from './App';
import {BrowserRouter} from 'react-router-dom';
import {history, store} from "./helpers";

ReactDOM.render(
    <Provider store={store}>
      <BrowserRouter history={history}>
        <App/>
      </BrowserRouter>
    </Provider>
    , document.getElementById('root')
);