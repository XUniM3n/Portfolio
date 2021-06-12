import React from "react";
import {connect} from "react-redux";
import {eventService} from "../services/event.service";
import {Link, withRouter} from "react-router-dom";
import {history} from "../helpers";
import {alertActions} from "../actions";
import {betService} from "../services/bet.service";
import {authActions} from "../actions/auth.actions";

class EventDetailsPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      id: this.props.match.params.id,
      title: '',
      description: '',
      outcomes: [],
      form: {
        outcome: 0,
        isVirtualMoney: true,
        money: 0
      },
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
      form: {
        ...this.state.form,
        [name]: value
      }
    });
  }

  handleSubmit(event) {
    event.preventDefault();

    this.setState({submitted: true});
    const {form} = this.state;
    const {dispatch} = this.props;

    let isEnoughMoney = false;

    if (form.isVirtualMoney) {
      if (this.props.auth.profile.virtualMoney >= form.money)
        isEnoughMoney = true;
      else
        dispatch(alertActions.error("Not enough money"));
    } else {
      if (this.props.auth.profile.realMoney >= form.money)
        isEnoughMoney = true;
      else
        dispatch(alertActions.error("Not enough money"));
    }

    if (isEnoughMoney)
      if (form.outcome && form.isVirtualMoney && form.money) {
        betService.add(this.state.id, form.outcome, form.money, form.isVirtualMoney)
            .then(response => {
              dispatch(authActions.subtractMoney(form.money, form.isVirtualMoney));
              history.push('/');
              dispatch(alertActions.success(response.message));
            }, error => {
              dispatch(alertActions.error(error));
            });
      }
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
            <h5 className="card-title">{this.state.title}</h5>
            <p className="card-text">{this.state.description}</p>
            <div>Make bet</div>
            <form onSubmit={this.handleSubmit}>
              {outcomeViews}
              <div><input type="checkbox" id="isVirtualMoney" name="isVirtualMoney" onChange={this.handleChange}
                          value={this.state.form.isVirtualMoney}/><span className="ml-2">Virtual money?</span>
              </div>
              <div className="control-group">
                <label className="control-label" htmlFor="money">Money</label>
                <div className="controls">
                  <input id="money" name="money" placeholder="" className="form-control input-lg m-2" type="text"
                         onChange={this.handleChange} value={this.state.form.money}/>
                </div>
              </div>
              <input type="submit" className="btn btn-success" value="Make Bet"/>
              <Link to="/" className="btn btn-link">Back to main page</Link>
            </form>
          </div>
        </div>
    );
  }
}

function mapStateToProps(state) {
  const {auth} = state;
  return {auth};
}

const connectedEventDetails = withRouter(connect(mapStateToProps)(EventDetailsPage));
export {connectedEventDetails as EventDetailsPage};