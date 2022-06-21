import React from 'react';
import { createGlobalStyle } from 'styled-components';
import { IndexPage } from './page';

const GlobalStyles = createGlobalStyle`
  :root {
    --black: #080c0b;
    --white: #f1f0ea;
    --gray: #d9d9d9;
    --gray-dark: #a3a3a3;
  }
  body {
    background-color: var(--white);
    color: var(--black);
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 
      'Roboto', 'Cantarell', sans-serif;
  }
`;

export const App = () => {
  return (
    <>
      <GlobalStyles />
      <IndexPage />
    </>
  );
};

export default App;
