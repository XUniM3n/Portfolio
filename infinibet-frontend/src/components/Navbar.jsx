import React from "react";
import {connect} from "react-redux";
import {Link} from "react-router-dom";
import {authActions} from "../actions";

class Navbar extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      activeLink: ''
    };

    this.logout = this.logout.bind(this);
  }

  logout() {
    this.props.dispatch(authActions.logout());
  }

  render() {
    const {profile} = this.props.auth;
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
          <Link className="navbar-brand" to="/">INFINIBET</Link>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav mr-auto">
              <li className="nav-item"><Link className="nav-link" to="/">Events</Link></li>
              <li className="nav-item"><Link className="nav-link" to="/event-suggestions">Suggestions</Link></li>
            </ul>
            {profile != null ?
                <ul className="navbar-nav ml-auto">
                  <li className="nav-link">{profile.username}</li>
                  <li className="nav-item"><Link to="/change-password" className="nav-link">Change
                    password</Link></li>
                  <li className="nav-item"><Link to="/logout" className="nav-link"
                                                 onClick={this.logout}>Logout</Link></li>
                </ul>
                : null
            }
          </div>
        </nav>
    )
  }
}

function mapStateToProps(state) {
  const {auth} = state;
  return {
    auth
  }
}

const connectedNavbar = connect(mapStateToProps)(Navbar);
export {connectedNavbar as Navbar};
