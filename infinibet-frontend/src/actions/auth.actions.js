import {actionConstants} from '../constants';
import {authService} from '../services';
import {alertActions} from './';
import {history} from '../helpers';
import {personService} from "../services/person.service";

export const authActions = {
  login,
  logout,
  signup,
  loadCurrentPersonProfile,
  subtractMoney
};

function login(username, password) {
  return dispatch => {
    dispatch(request(username));

    authService.login(username, password)
        .then(
            token => {
              dispatch(success(token));

              dispatch(profileRequest());

              personService.getCurrentPersonProfile()
                  .then(profile => {
                    history.push('/');
                    dispatch(profileSuccess(profile));
                  }, error => {
                    dispatch(logout());
                    dispatch(alertActions.error("Failed to load your profile"));
                  });

              function profileRequest() {
                return {type: actionConstants.AUTH_LOAD_PROFILE_REQUEST};
              }

              function profileSuccess(profile) {
                return {type: actionConstants.AUTH_LOAD_PROFILE_SUCCESS, profile};
              }
            },
            error => {
              dispatch(failure(error.toString()));
              dispatch(alertActions.error(error.toString()));
            }
        )
  };

  function request(username) {
    return {type: actionConstants.AUTH_LOGIN_REQUEST, username};
  }

  function success(token) {
    return {type: actionConstants.AUTH_LOGIN_SUCCESS, token};
  }

  function failure(error) {
    return {type: actionConstants.AUTH_LOGIN_FAILURE, error};
  }
}

function logout() {
  authService.logout();
  return {type: actionConstants.AUTH_LOGOUT};
}

function signup(username, password, passwordConfirmation) {
  return dispatch => {
    dispatch(request());

    authService.signup(username, password, passwordConfirmation)
        .then(
            body => {
              dispatch(success());
              history.push('/login');
              dispatch(alertActions.success('Registration successful'));
            },
            error => {
              dispatch(failure(error));
              dispatch(alertActions.error(error));
            }
        );
  };

  function request() {
    return {type: actionConstants.AUTH_SIGNUP_REQUEST};
  }

  function success() {
    return {type: actionConstants.AUTH_SIGNUP_SUCCESS};
  }

  function failure(error) {
    return {type: actionConstants.AUTH_SIGNUP_FAILURE, error};
  }
}

function loadCurrentPersonProfile() {
  return dispatch => {

    dispatch(profileRequest());

    personService.getCurrentPersonProfile()
        .then(profile => {
          dispatch(profileSuccess(profile));
        }, error => {
          dispatch(logout());
          dispatch(alertActions.error("Failed to load your profile"));
        });

    function profileRequest() {
      return {type: actionConstants.AUTH_LOAD_PROFILE_REQUEST};
    }

    function profileSuccess(profile) {
      return {type: actionConstants.AUTH_LOAD_PROFILE_SUCCESS, profile};
    }
  }
}

function subtractMoney(money, isVirtualMoney) {
  return {type: actionConstants.AUTH_SUBTRACT_MONEY, money, isVirtualMoney};
}