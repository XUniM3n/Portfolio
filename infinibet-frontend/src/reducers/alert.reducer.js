import {actionConstants} from '../constants';

export function alert(state = {}, action) {
  switch (action.type) {
    case actionConstants.ALERT_SUCCESS:
      return {
        type: 'alert-success',
        message: action.message
      };
    case actionConstants.ALERT_ERROR:
      return {
        type: 'alert-danger',
        message: action.message
      };
    case actionConstants.ALERT_CLEAR:
      return {};
    default:
      return state
  }
}