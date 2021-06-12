import React from "react";
import {connect} from "react-redux";
import {history} from "../helpers";
import {alertActions} from "../actions";
import {eventSuggestionService} from "../services/event-suggestion.service";
import {Link} from "react-router-dom";

class SuggestEventPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      form: {
        title: '',
        form: ''
      },
      submitted: false,
      submitting: false
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
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
    const {form} = this.state;
    const {dispatch} = this.props;
    console.log(JSON.stringify(form));
    if (form.title && form.description) {
      eventSuggestionService.add(form.title, form.description)
          .then(message => {
            history.push('/event-suggestions');
            dispatch(alertActions.success(message));
          }, error => {
            dispatch(alertActions.error(error));
          });
    }
  }

  render() {
    return (
        <div className="row">
          <form name="form" onSubmit={this.handleSubmit} method="POST">
            <div className="control-group my-4">
              <label className="control-label" htmlFor="title">Title</label>
              <div className="controls">
                <input id="title" name="title" placeholder="" className="form-control input-lg"
                       type="text" onChange={this.handleChange}/>
              </div>
            </div>
            <div className="control-group my-4">
              <label className="control-label" htmlFor="description">Description</label>
              <div className="controls">
                <textarea id="description" name="description" placeholder=""
                          className="form-control input-lg" onChange={this.handleChange}/>
              </div>
            </div>
            <input type="submit" className="btn btn-primary" value="Suggest event"/>
            <Link to="/" className="btn btn-link">Back to main page</Link>
          </form>
        </div>
    );
  }
}

function mapStateToProps(state) {
  return {};
}

const connectedSuggestEventPage = connect(mapStateToProps)(SuggestEventPage);
export {connectedSuggestEventPage as SuggestEventPage};