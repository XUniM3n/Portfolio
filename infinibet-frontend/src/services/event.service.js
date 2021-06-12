import {API_BASE_URL, API_EVENT_SUFFIX, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE} from '../constants';
import {apiGetRequest, apiPostRequest} from "../helpers";

export const eventService = {
  getById,
  getPage,
  add,
  setResult,
};

const API_EVENT_URL = API_BASE_URL + API_EVENT_SUFFIX;

function getById(id) {
  return apiGetRequest(`${API_EVENT_URL}/${id}`);
}

function getPage(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) {
  return apiGetRequest(`${API_EVENT_URL}?page=${page}&size=${size}`);
}

function add(title, description, outcomes) {
  return apiPostRequest(`${API_EVENT_URL}/add`, {title, description, outcomes});
}

function setResult(eventId, outcomeId) {
  return apiPostRequest(`${API_EVENT_URL}/set-result`, {eventId, outcomeId});
}
