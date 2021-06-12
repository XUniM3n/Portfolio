import {Redirect, Route} from "react-router-dom";
import React from "react";

export const PrivateRoute = ({component: Component, auth, personRole, ...rest}) => (
    <Route {...rest} render={props => {
      if ((auth && auth.profile && (!personRole || (auth.profile.roles && auth.profile.roles.includes(personRole))))) {
        return (<Component {...props} />);
      } else {
        return (<Redirect to={{pathname: '/', state: {from: props.location}}}/>);
      }
    }}/>);