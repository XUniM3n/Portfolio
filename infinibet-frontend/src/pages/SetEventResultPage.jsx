import React from "react";
import {connect} from "react-redux";
import {Link} from "react-router-dom";
import {eventService} from "../services/event.service";
import {alertActions} from "../actions";
import {history} from "../helpers";

class SetEventResultPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      id: this.props.match.params.id,
      title: '',
      description: '',
      outcomes: [],
      outcome: 0,
      submitted: false,
      submitting: false
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    const {dispatch} = this.props;

    eventService.getById(this.state.id)
        .then(event => {
          this.setState({
            title: event.title,
            description: event.description,
            outcomes: event.outcomes,
          });
        }, error => {
          dispatch(alertActions.error(error));
        });
  }

  handleChange(event) {
    const {name, value} = event.target;
    this.setState({
      ...this.state,
      [name]: value
    });
  }

  handleSubmit(event) {
    event.preventDefault();

    this.setState({submitted: true});
    const {dispatch} = this.props;

    eventService.setResult(this.state.id, this.state.outcome)
        .then(response => {
          history.push("/");
          dispatch(alertActions.success(response.message));
        }, error => {
          dispatch(alertActions.error(error));
        });
  }

  render() {
    const outcomeViews = this.state.outcomes.map((outcome, id) => (
        <li key={id}>
          <input type="radio" name="outcome" value={outcome.id}
                 onChange={this.handleChange}/>{outcome.name} | {outcome.coefficient}
        </li>
    ));

    return (
        <div className="card">
          <div className="card-body">
            <h5 className="card-title">{this.props.title}</h5>
            <p className="card-text">{this.props.description}</p>
            <div>Make bet</div>
            <form onSubmit={this.handleSubmit}>
              {outcomeViews}
              <input type="submit" className="btn btn-danger" value="Submit Result"/>
              <Link to="/" className="btn btn-link">Back to main page</Link>
            </form>
          </div>
        </div>
    );
  }
}

function mapStateToProps(state) {
  return {};
}

const connectedSetEventResultPage = connect(mapStateToProps)(SetEventResultPage);
export {connectedSetEventResultPage as SetEventResultPage};