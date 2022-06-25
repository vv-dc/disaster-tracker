// implements Store interface

export const saveToStore = (key, value) => {
  const serialized = JSON.stringify(value);
  localStorage.setItem(key, serialized);
};

export const removeFromStore = (key) => {
  localStorage.removeItem(key);
};

export const getFromStore = (key) => {
  const entry = localStorage.getItem(key);
  return JSON.parse(entry);
};
