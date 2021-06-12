import React from "react";
import {connect} from "react-redux";
import {Link} from "react-router-dom";
import {history} from "../helpers/history"
import {eventService} from "../services/event.service";
import {alertActions} from "../actions/alert.actions";
import {ModeratorOutcome} from "../components/Outcome.Moderator";

class AddEventPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      form: {
        title: '',
        description: '',
        outcomeName: '',
        outcomeCoefficient: 0.0,
      },
      outcomes: [],
      submitted: false,
      submitting: false
    };

    this.addOutcome = this.addOutcome.bind(this);
    this.deleteOutcome = this.deleteOutcome.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  addOutcome(e) {
    e.preventDefault();

    const {outcomes} = this.state;

    this.setState({
          ...this.state,
          outcomes: outcomes.slice().concat(
              {name: this.state.form.outcomeName, coefficient: this.state.form.outcomeCoefficient}),
          form: {
            ...this.state.form,
            outcomeName: '',
            outcomeCoefficient: 0.0,
          }
        }
    );
  }

  deleteOutcome(e, id) {
    e.preventDefault();

    this.setState({
          ...this.state,
          outcomes: this.state.outcomes.filter(function (e, i) {
            return i !== id;
          })
        }
    );
  }

  handleChange(event) {
    const {name, value} = event.target;
    const {form} = this.state;
    this.setState({
      ...this.state,
      form: {
        ...form,
        [name]: value
      }
    });
  }

  handleSubmit(event) {
    event.preventDefault();

    this.setState({submitted: true});
    const {form, outcomes} = this.state;
    const {dispatch} = this.props;
    console.log('form.title:' + JSON.stringify(form.title));
    console.log('form.description:' + JSON.stringify(form.description));
    console.log('outcomes:' + JSON.stringify(outcomes));
    if (form.title && form.description && outcomes) {
      eventService.add(form.title, form.description, outcomes)
          .then(message => {
            history.push('/');
            alertActions.success(message);
          }, error => {
            dispatch(alertActions.error(error));
          });
    }
  }

  render() {
    const outcomeViews = this.state.outcomes.map((outcome, id) => (
        <li key={id}>
          <ModeratorOutcome name={outcome.name} coefficient={outcome.coefficient}/>
          <button className="btn btn-danger ml-2" onClick={(e) => this.deleteOutcome(e, id)}>Delete
          </button>
        </li>
    ));

    return (
        <div className="row">
          <form name="form" onSubmit={this.handleSubmit}>
            <div className="control-group my-4">
              <label className="control-label" htmlFor="title">Title</label>
              <div className="controls">
                <input id="title" name="title" placeholder="" className="form-control input-lg" type="text"
                       onChange={this.handleChange}/>
              </div>
            </div>
            <div className="control-group my-4">
              <label className="control-label" htmlFor="description">Description</label>
              <div className="controls">
                <textarea id="description" name="description" placeholder=""
                          className="form-control input-lg" onChange={this.handleChange}/>
              </div>
            </div>
            <div className="control-group my-4">
              <span>Outcomes</span>
              <ul id="outcomes">
                {outcomeViews}
              </ul>
              <div>
                <label className="control-label" htmlFor="outcomeName">Name</label>
                <input type="text" id="outcomeName" name="outcomeName" ref="name" value={this.state.form.outcomeName}
                       onChange={this.handleChange}/>
                <label className="control-label" htmlFor="outcomeCoefficient">Coefficient</label>
                <input type="text" id="outcomeCoefficient" name="outcomeCoefficient" ref="outcomeCoefficient"
                       value={this.state.form.outcomeCoefficient} onChange={this.handleChange}/>
                <button className="btn btn-sm btn-primary" onClick={(e) => this.addOutcome(e)}>Add</button>
              </div>

            </div>
            <input type="submit" className="btn btn-primary" value="Add Event"/>
            <Link to="/" className="btn btn-link">Back to main page</Link>
          </form>
        </div>
    );
  }
}

function mapStateToProps(state) {
  return {};
}

const connectedAddEventPage = connect(mapStateToProps)(AddEventPage);
export {connectedAddEventPage as AddEventPage};