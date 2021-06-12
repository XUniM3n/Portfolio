import {actionConstants} from '../constants';

let token = JSON.parse(localStorage.getItem('user'));
const initialState = token ? {loggedIn: true, loadedProfile: false, token} : {};

export function auth(state = initialState, action) {
  switch (action.type) {
    case actionConstants.AUTH_LOGIN_REQUEST:
      return {
        loggingIn: true,
        loadedProfile: false,
        username: action.username
      };
    case actionConstants.AUTH_LOGIN_SUCCESS:
      return {
        loggedIn: true,
        loadedProfile: false,
        token: action.token,
      };
    case actionConstants.AUTH_LOGIN_FAILURE:
      return {};
    case actionConstants.AUTH_LOGOUT:
      return {};
    case actionConstants.AUTH_SIGNUP_REQUEST:
      return {registering: true};
    case actionConstants.AUTH_SIGNUP_SUCCESS:
      return {};
    case actionConstants.AUTH_SIGNUP_FAILURE:
      return {};
    case actionConstants.AUTH_LOAD_PROFILE_REQUEST:
      return {
        ...state,
        loadingProfile: true
      };
    case actionConstants.AUTH_LOAD_PROFILE_SUCCESS:
      return {
        ...state,
        loadedProfile: true,
        profile: action.profile
      };
    case actionConstants.AUTH_LOAD_PROFILE_FAILURE:
      return {};
    case actionConstants.AUTH_SUBTRACT_MONEY:
      if (action.isVirtualMoney) {
        return ({
          ...state,
          profile: {
            ...state.profile,
            virtualMoney: state.profile.virtualMoney - action.money
          }
        });
      } else {
        return ({
          ...state,
          profile: {
            ...state.profile,
            realMoney: state.profile.realMoney - action.money
          }
        });
      }
    default:
      return state
  }
}