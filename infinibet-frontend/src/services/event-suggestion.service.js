import {apiGetRequest, apiPostRequest} from "../helpers";
import {API_BASE_URL, API_EVENT_SUGGESTION_SUFFIX, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE} from "../constants";

export const eventSuggestionService = {
  getById,
  getPage,
  add,
  delete: _delete
};

const API_EVENT_SUGGESTION_URL = API_BASE_URL + API_EVENT_SUGGESTION_SUFFIX;

function getById(id) {
  return apiGetRequest(`${API_EVENT_SUGGESTION_URL}/${id}`);
}

function getPage(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) {
  return apiGetRequest(`${API_EVENT_SUGGESTION_URL}?page=${page}&size=${size}`);
}

function add(title, description) {
  return apiPostRequest(`${API_EVENT_SUGGESTION_URL}/add`, {title, description});
}

function _delete(id) {
  return apiPostRequest(`${API_EVENT_SUGGESTION_URL}/delete/${id}`, {id});
}
