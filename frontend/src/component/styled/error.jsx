import styled from 'styled-components';

export const ErrorBlock = styled.div.attrs(() => ({
  role: 'alert',
}))`
  padding: 20px 30px;
  background-color: var(--error-red);
  border: 1px solid var(--gray-dark);
  border-radius: 6px;
  text-align: center;
  font-size: 13px;
`;
