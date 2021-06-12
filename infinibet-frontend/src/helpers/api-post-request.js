import {authActions} from "../actions";

export function apiPostRequest(url, body) {
  let headers = new Headers();
  headers.append('Content-Type', 'application/json');

  let user = JSON.parse(localStorage.getItem('user'));
  if (user)
    headers.append('Authorization', 'Bearer ' + user);

  const requestOptions = {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(body)
  };

  return fetch(url, requestOptions)
      .then(response =>
          response.json().then(json => {
            if (!response.ok) {
              const error = (json && json.message) || response.statusText;
              if (response.status === 401) {
                authActions.logout();
                // eslint-disable-next-line no-restricted-globals
                location.reload();
              }

              return Promise.reject(error);
            }
            if (json.status != null && json.status !== true)
              return Promise.reject(json.message);

            return json;
          })
      );
}
