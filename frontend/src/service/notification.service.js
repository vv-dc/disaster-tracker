import { getAccessToken } from './auth.service';
import { getRequest } from './http.service';

export const getNotifications = async ({ calendarId, timeMin, timeMax }) => {
  const accessToken = getAccessToken();
  const url = 'http://localhost:8080/api/events/google';
  const auth = `Bearer ${accessToken}`;
  const query = { calendarId, timeMin, timeMax };
  return getRequest(url, query, { Authorization: auth });
};
