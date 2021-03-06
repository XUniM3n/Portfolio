import React from 'react';
import {Link} from 'react-router-dom';
import {connect} from 'react-redux';

import {authActions} from '../actions';

class SignUpPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      user: {
        username: '',
        password: '',
        passwordConfirmation: ''
      },
      submitted: false
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    const {name, value} = event.target;
    const {user} = this.state;
    this.setState({
      user: {
        ...user,
        [name]: value
      }
    });
  }

  handleSubmit(event) {
    event.preventDefault();

    this.setState({submitted: true});
    const {user} = this.state;
    const {dispatch} = this.props;
    if (user.username && user.password) {
      dispatch(authActions.signup(user.username, user.password, user.passwordConfirmation));
    }
  }

  render() {
    const {registering} = this.props;
    const {user, submitted} = this.state;
    return (
        <div className="col-md-6 col-md-offset-3">
          <h2>Register</h2>
          <form name="form" onSubmit={this.handleSubmit}>
            <div className={'form-group' + (submitted && !user.username ? ' has-error' : '')}>
              <label htmlFor="username">Username</label>
              <input type="text" className="form-control" name="username" value={user.username}
                     onChange={this.handleChange}/>
              {submitted && !user.username &&
              <div className="help-block">Username is required</div>
              }
            </div>
            <div className={'form-group' + (submitted && !user.password ? ' has-error' : '')}>
              <label htmlFor="password">Password</label>
              <input type="password" className="form-control" name="password" value={user.password}
                     onChange={this.handleChange}/>
              {submitted && !user.password &&
              <div className="help-block">Password is required</div>
              }
            </div>
            <div className={'form-group' + (submitted && !user.passwordConfirmation ? ' has-error' : '')}>
              <label htmlFor="password">Password Confirmation</label>
              <input type="password" className="form-control" name="passwordConfirmation"
                     value={user.passwordConfirmation} onChange={this.handleChange}/>
              {submitted && !user.passwordConfirmation &&
              <div className="help-block">Password Confirmation is required</div>
              }
            </div>
            <div className="form-group">
              <button className="btn btn-primary">Register</button>
              <Link to="/login" className="btn btn-link">Login instead</Link>
            </div>
          </form>
        </div>
    );
  }
}

function mapStateToProps(state) {
  const {registering} = state.auth;
  return {
    registering
  };
}

const connectedSignUpPage = connect(mapStateToProps)(SignUpPage);
export {connectedSignUpPage as SignUpPage};