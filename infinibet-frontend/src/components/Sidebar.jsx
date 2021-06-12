import React, {Component} from "react";
import {connect} from "react-redux";
import {Link} from "react-router-dom";
import {ROLE_MODERATOR, ROLE_PLAYER} from "../constants";

class Sidebar extends Component {
  constructor(props) {
    super(props);

    this.state = {
      activeLink: ''
    };
  }

  render() {
    const {auth} = this.props;
    if (auth.loadedProfile) {
      if (auth.profile.roles.includes(ROLE_PLAYER)) {
        return (
            <div className="col-md-4 card py-3">
              <div className="card-body">
                <div>
                  <h5>Real money <span className="money-amount">{auth.profile.realMoney.toString()}</span></h5>
                </div>
                <div>
                  <h5>Virtual money <span
                      className="money-amount">{auth.profile.virtualMoney.toString()}</span>
                  </h5>
                </div>
                <Link className="btn btn-sm btn-block btn-primary mb-1" to="/balance">Add/Withdraw money</Link>
                <Link className="btn btn-sm btn-block btn-info mb-1" to="/bets">Bets</Link>
                <Link className="btn btn-sm btn-block btn-info mb-1" to="/transactions">Transactions</Link>
                <Link className="btn btn-sm btn-block btn-primary mb-1" to="/suggest-event">Suggest event</Link>
              </div>
            </div>
        );
      } else if (auth.profile.roles.includes(ROLE_MODERATOR)) {
        return (
            <div className="col-md-4 card py-3">
              <div className="card-body">
                <Link className="btn btn-sm btn-block btn-primary mb-1" to="/add-event">Add event</Link>
              </div>
            </div>
        );
      }
    } else return (
        <div className="col-md-4 card py-3">
          <div className="card-body">
            <div className="mb-3 font-weight-bold text-center">You are not logged in</div>
            <Link className="btn btn-sm btn-block btn-primary mb-1" to="/login">Login</Link>
            <Link className="btn btn-sm btn-block btn-primary mb-1" to="/signup">Create account</Link>
          </div>
        </div>
    )
  }
}

function mapStateToProps(state) {
  const {auth} = state;
  return {
    auth
  };
}

const connectedSidebar = connect(mapStateToProps)(Sidebar);
export {connectedSidebar as Sidebar};
