import React from "react";
import {connect} from "react-redux";
import {history} from '../helpers';
import {Link} from "react-router-dom";
import {authService} from "../services/auth.service";
import {alertActions} from "../actions/alert.actions";

class ChangePasswordPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      form: {
        oldPassword: '',
        newPassword: '',
        newPasswordConfirmation: ''
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
    if (form.oldPassword && form.newPassword && form.newPasswordConfirmation) {
      authService.changePassword(form.oldPassword, form.newPassword, form.newPasswordConfirmation)
          .then(response => {
            history.push('/');
            dispatch(alertActions.success(response.message));
          }, error => {
            dispatch(alertActions.error(error));
          });
    }
  }

  render() {
    const {form, submitted, submitting} = this.state;

    return (
        <div className="col-md-6 col-md-offset-3">
          <h2>Change password</h2>
          <form name="form" onSubmit={this.handleSubmit}>
            <div className={'form-group' + (submitted && !form.oldPassword ? ' has-error' : '')}>
              <label htmlFor="password">Old Password</label>
              <input type="password" className="form-control" name="oldPassword" value={form.oldPassword}
                     onChange={this.handleChange}/>
              {submitted && !form.oldPassword &&
              <div className="help-block">Old Password is required</div>
              }
            </div>
            <div className={'form-group' + (submitted && !form.newPassword ? ' has-error' : '')}>
              <label htmlFor="newPassword">New Password</label>
              <input type="password" className="form-control" name="newPassword"
                     value={form.newPassword} onChange={this.handleChange}/>
              {submitted && !form.newPassword &&
              <div className="help-block">New Password is required</div>
              }
            </div>
            <div className={'form-group' + (submitted && !form.newPasswordConfirmation ? ' has-error' : '')}>
              <label htmlFor="newPasswordConfirmation">New Password Confirmation</label>
              <input type="password" className="form-control" name="newPasswordConfirmation"
                     value={form.newPasswordConfirmation} onChange={this.handleChange}/>
              {submitted && !form.newPasswordConfirmation &&
              <div className="help-block">New Password Confirmation is required</div>
              }
            </div>
            <div className="form-group">
              <button className="btn btn-primary">Change Password</button>
              {submitting &&
              <img src="#" alt="alt"/>
              }
              <Link to="/" className="btn btn-link">Back to Main Page</Link>
            </div>
          </form>
        </div>
    );
  }
}

function mapStateToProps(state) {
  return {};
}

const connectedChangePasswordPage = connect(mapStateToProps)(ChangePasswordPage);
export {connectedChangePasswordPage as ChangePasswordPage};