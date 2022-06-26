import React from 'react';
import styled from 'styled-components';
import { config } from '../../config/config';
import { PopupWindow } from '../../lib/popup-window';
import { ReactComponent as GoogleLogo } from './google-logo.svg';
import { BaseButton } from '../styled/control';

export const GoogleLoginButton = ({ buttonText, onSuccess, onFailure }) => {
  const { host, googleInitId, successUrl, failureUrl } = config.api.auth;

  const handleClick = async (event) => {
    event.preventDefault();
    const url = buildBackendUrl();
    PopupWindow('google-auth', url).then(onSuccess).catch(onFailure);
  };

  const buildBackendUrl = () => {
    const params = new URLSearchParams({
      disaster_success_url: successUrl,
      disaster_failure_url: failureUrl,
    });
    return `${host}/auth/oauth/init/${googleInitId}?${params.toString()}`;
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
