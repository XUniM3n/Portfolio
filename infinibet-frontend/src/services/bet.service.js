import {apiGetRequest, apiPostRequest} from "../helpers";
import {API_BASE_URL, API_BET_SUFFIX, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE} from "../constants";

export const betService = {
  add,
  getPage
};

const API_BET_URL = API_BASE_URL + API_BET_SUFFIX;

function getPage(username, page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) {
  return apiGetRequest(`${API_BET_URL}?page=${page}&size=${size}&username=${username}`);
}

function add(eventId, outcomeId, money, isVirtualMoney) {
  return apiPostRequest(`${API_BET_URL}/add`, {eventId, outcomeId, money, isVirtualMoney});
}
