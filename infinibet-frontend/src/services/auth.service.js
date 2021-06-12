import {API_AUTH_SUFFIX, API_BASE_URL} from '../constants';
import {apiPostRequest} from '../helpers';

export const authService = {
  login,
  signup,
  logout,
  changePassword
};

const API_AUTH_URL = API_BASE_URL + API_AUTH_SUFFIX;

function login(username, password) {
  return apiPostRequest(`${API_AUTH_URL}/login`, {username, password})
      .then(body => {
        localStorage.setItem('user', JSON.stringify(body.accessToken));

        return body;
      });
}

function signup(username, password, passwordConfirmation) {
  return apiPostRequest(`${API_AUTH_URL}/signup`, {username, password, passwordConfirmation});
}

function changePassword(oldPassword, newPassword, newPasswordConfirmation) {
  return apiPostRequest(`${API_AUTH_URL}/change-password`,
      {oldPassword, newPassword, newPasswordConfirmation});
}

function logout() {
  // remove user from local storage to log user out
  localStorage.removeItem('user');
}
