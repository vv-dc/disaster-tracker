import React from 'react';
import { createGlobalStyle } from 'styled-components';
import { IndexPage } from './page';

const GlobalStyles = createGlobalStyle`
  :root {
    --black: #080c0b;
    --white: #f1f0ea;
    --gray: #c8c8c8;
    --gray-dark: #18191a;
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
