import React from 'react';
import './App.css';

import {Route, Switch} from "react-router-dom";
import {
  AddEventPage,
  ChangePasswordPage,
  EventDetailsPage,
  EventSuggestionsPage,
  HomePage,
  LoginPage,
  SetEventResultPage,
  SignUpPage,
  SuggestEventPage
} from "./pages";
import {connect} from "react-redux";
import {history} from "./helpers";
import {alertActions} from "./actions/alert.actions";
import {Navbar} from "./components/Navbar";
import {Sidebar} from "./components/Sidebar";
import {authActions} from "./actions/auth.actions";
import 'bootstrap/dist/css/bootstrap.min.css';
import {PrivateRoute} from "./components";
import {actionConstants, ROLE_MODERATOR, ROLE_PLAYER} from "./constants";
import {personService} from "./services/person.service";

class App extends React.Component {
  constructor(props) {
    super(props);

    const {dispatch} = this.props;
    history.listen((location, action) => {
      // clear alert on location change
      dispatch(alertActions.clear());
    });

    this.clearAlert = this.clearAlert.bind(this);
    this.loadCurrentPersonProfile = this.loadCurrentPersonProfile.bind(this);

    this.loadCurrentPersonProfile();
  }

  loadCurrentPersonProfile() {
    if (this.props.auth.loggedIn && this.props.auth.profile == null) {
      personService.getCurrentPersonProfile()
          .then(profile => {
            this.props.dispatch({type: actionConstants.AUTH_LOAD_PROFILE_SUCCESS, profile});
          }, error => {
            this.props.dispatch(authActions.logout());
            this.props.dispatch(alertActions.error("Failed to load your profile"));
          });
    }
  }

  clearAlert() {
    this.props.dispatch(alertActions.clear);
  }

  render() {
    const {alert, auth} = this.props;
    return (
        <div className="bg-light">
          <Navbar/>
          <div className="container my-2">
            <div className="row">
              <div className="col-md-12">
                {alert.message &&
                <div className={`alert ${alert.type}`}
                     onClick={this.clearAlert}>{alert.message.toString()}</div>
                }
              </div>
            </div>
            <div className="row">
              <div className="col-md-8 px-2">
                <Switch>
                  <Route exact path="/" component={HomePage}/>
                  <PrivateRoute path="/add-event" component={AddEventPage} personRole={ROLE_MODERATOR} auth={auth}/>
                  <PrivateRoute path="/change-password" component={ChangePasswordPage} auth={auth}/>
                  <PrivateRoute path="/events/:id" component={EventDetailsPage} personRole={ROLE_PLAYER} auth={auth}/>
                  <PrivateRoute path="/moderation/events/:id" component={SetEventResultPage} personRole={ROLE_MODERATOR}
                                auth={auth}/>
                  <Route path="/event-suggestions" component={EventSuggestionsPage}/>
                  <Route path="/login" component={LoginPage}/>
                  <Route path="/signup" component={SignUpPage}/>
                  <PrivateRoute path="/suggest-event" component={SuggestEventPage} personRole={ROLE_PLAYER}
                                auth={auth}/>
                </Switch>
              </div>
              <Sidebar/>
            </div>
            <br/>
            <hr/>
            <div className="row">
              <div className="text-center col-lg-6 offset-lg-3">
                <p>INFINIBET &copy; 2019 &middot; No Rights Reserved</p>
              </div>
            </div>
          </div>
        </div>
    );
  }
}

function mapStateToProps(state) {
  const {alert, auth} = state;
  return {
    alert,
    auth
  };
}

const connectedApp = connect(mapStateToProps)(App);
export {connectedApp as App};
