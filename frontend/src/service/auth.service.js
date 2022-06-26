import * as store from '../store/localStorageStore';

const LOGGED_USER_STORAGE_KEY = 'disaster_logged_in_user';

export const setUserLoggedIn = ({ accessToken, email }) => {
  store.saveToStore(LOGGED_USER_STORAGE_KEY, { accessToken, email });
};

export const setUserLogout = () => {
  store.removeFromStore(LOGGED_USER_STORAGE_KEY);
};

export const getAccessToken = () => {
  const user = store.getFromStore(LOGGED_USER_STORAGE_KEY);
  return user?.accessToken;
};

export const getUser = () => {
  return store.getFromStore(LOGGED_USER_STORAGE_KEY);
};

export const isLoggedIn = () => {
  return Boolean(getAccessToken());
};
