import React from "react";
import {Link} from "react-router-dom";
import {connect} from "react-redux";
import {ROLE_MODERATOR} from "../constants";

class Event extends React.Component {

  render() {
    const {id, title, description, auth} = this.props;

    if (auth && auth.profile && auth.profile.roles && auth.profile.roles.includes(ROLE_MODERATOR)) {
      return (
          <div className="card mb-1">
            <div className="card-body">
              <h5 className="card-title"><Link to={{pathname: `/moderation/events/${id}`}}> {title}</Link></h5>
              <p className="card-text">{description}</p>
              <Link className="btn btn-primary" to={{pathname: `/moderation/events/${id}`}}>More Info</Link>
            </div>
          </div>
      );
    }

    return (
        <div className="card mb-1">
          <div className="card-body">
            <h5 className="card-title"><Link to={{pathname: `/events/${id}`}}> {title}</Link></h5>
            <p className="card-text">{description}</p>
            <Link className="btn btn-primary" to={{pathname: `/events/${id}`}}>More Info</Link>
          </div>
        </div>
    );
  }
}

function mapStateToProps(state) {
  const {auth} = state;
  return {auth};
}

const connectedEvent = connect(mapStateToProps)(Event);
export {connectedEvent as Event}