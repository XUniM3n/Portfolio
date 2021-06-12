import {apiGetRequest} from "../helpers";
import {API_BASE_URL, API_PERSON_SUFFIX} from "../constants";

export const personService = {
  getCurrentPersonProfile
};

const API_USER_SERVICE = API_BASE_URL + API_PERSON_SUFFIX;

function getCurrentPersonProfile() {
  return apiGetRequest(`${API_USER_SERVICE}/me`);
}