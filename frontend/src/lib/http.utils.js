export const buildQueryParams = (query) => {
  const queryParts = [];
  for (const [key, value] of Object.entries(query)) {
    const part = `${key}=${value}`;
    queryParts.push(part);
  }
  return queryParts.join('&');
};
