import React from 'react';
import styled from 'styled-components';
import { ReactComponent as GoogleLogo } from './google-logo.svg';
import { BaseButton } from '../styled/control';

export const GoogleLoginButton = ({ buttonText, onSuccess }) => {
  const handleClick = (event) => {
    event.preventDefault();
    console.log(`Clicked on: ${buttonText}`);
    onSuccess();
  };

  return (
    <ButtonBody onClick={handleClick}>
      {buttonText} <GoogleLogo />
    </ButtonBody>
  );
};

const ButtonBody = styled(BaseButton)`
  background-color: var(--white);
  color: var(--gray-dark);
  line-height: 18px;
  padding: 7px 15px;

  svg {
    height: 18px;
    margin-left: 8px;
    vertical-align: bottom;
  }
`;
