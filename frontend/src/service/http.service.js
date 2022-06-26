import { config } from '../config/config';
import { HttpError } from '../lib/httpErrors';

export const constructApiUrl = (service, endpoint) => {
  const baseUrl = config.api[service];
  return `${baseUrl}${endpoint}`;
};

export const getRequest = async (url, query, headers) => {
  const queryString = query ? '?' + buildQueryParams(query) : '';
  const urlWithQuery = url + queryString;

  const response = await fetch(urlWithQuery, {
    method: 'GET',
    headers: headers ?? {},
  });

  const payload = await response.json();
  if (!response.ok) throw buildHttpError(payload, response.status);

  return payload;
};

const buildHttpError = (payload, code) => {
  const reason = JSON.stringify(payload);
  return new HttpError(reason, code);
};

const buildQueryParams = (query) => {
  const queryParts = [];
  for (const [key, value] of Object.entries(query)) {
    const part = `${key}=${value}`;
    queryParts.push(part);
  }
  return queryParts.join('&');
};
