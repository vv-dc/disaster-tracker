export const config = {
  api: {
    auth: {
      host: process.env.REACT_APP_AUTH_BACKEND_HOST,
      googleInitId: process.env.REACT_APP_AUTH_GOOGLE_INIT_ID,
      successUrl: 'http://localhost:3000',
      failureUrl: 'http://localhost:3000/error',
    },
  },
};
