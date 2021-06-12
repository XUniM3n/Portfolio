import {actionConstants} from '../constants';

export const alertActions = {
  success,
  error,
  clear
};

function success(message) {
  return {type: actionConstants.ALERT_SUCCESS, message};
}

function error(message) {
  return {type: actionConstants.ALERT_ERROR, message};
}

function clear() {
  return {type: actionConstants.ALERT_CLEAR};
}