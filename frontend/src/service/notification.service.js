import { config } from '../config/config';
import { getAccessToken } from './auth.service';
import { buildQueryParams } from '../lib/http.utils';

export const getNotificationsStream = ({ calendarId, timeMin, timeMax }) => {
  const accessToken = getAccessToken();
  const apiUrl = buildNotificationsUrl({
    calendarId,
    timeMin,
    timeMax,
    accessToken,
  });
  return new EventSource(apiUrl);
};

export const buildNotificationsUrl = (query) => {
  const queryString = buildQueryParams(query);
  return `${config.api.notification.host}/api/events?${queryString}`;
};

export const parseNotification = (message) => {
  return JSON.parse(message.data);
};
