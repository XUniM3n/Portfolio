import React from "react";
import {ROLE_MODERATOR} from "../constants";
import {connect} from "react-redux";

class EventSuggestion extends React.Component {
  render() {
    const {title, description, auth, deleteFunc} = this.props;

    return (
        <div className="card mb-1">
          <div className="card-body">
            <h5 className="card-title">{title}</h5>
            <p className="card-text">{description}</p>
            {(auth && auth.profile && auth.profile.roles && auth.profile.roles.includes(ROLE_MODERATOR)) ?
                <button type="button" onClick={deleteFunc}/>
                : null}
          </div>
        </div>
    );
  }
}

function mapStateToProps(state) {
  const {auth} = state;
  return {auth};

}

const connectedEventSuggestion = connect(mapStateToProps)(EventSuggestion);
export {connectedEventSuggestion as EventSuggestion};

