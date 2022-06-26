import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import * as authService from '../../service/auth.service';
import { GoogleLoginButton } from '../google-login/google-login-button';
import { BaseButton, FormInput, FormLabel } from '../styled/control';

export const CalendarLoginForm = () => {
  const [error, setError] = useState();
  const [isLogged, setIsLogged] = useState(false);
  const [email, setEmail] = useState('');

  useEffect(() => {
    const isLogged = authService.isLoggedIn();
    if (isLogged) {
      setEmail(authService.getUser().email);
    }
    setIsLogged(isLogged);
  }, []);

  const handleLogin = (payload) => {
    authService.setUserLoggedIn(payload);
    setEmail(authService.getUser().email);
    setIsLogged(true);
  };

  const handleLogout = (event) => {
    event.preventDefault();
    authService.setUserLogout();
    setIsLogged(false);
  };

  return (
    <FormWrapper>
      <CalendarForm onSubmit={handleLogin}>
        {error ? (
          <div className="error-block" onClick={() => setError('')}>
            {error}
          </div>
        ) : null}
        {isLogged ? (
          <>
            <LoggedInParagraph>
              Logged in as <b>{email}</b>
            </LoggedInParagraph>
            <BaseButton onClick={handleLogout}>Logout</BaseButton>
          </>
        ) : (
          <GoogleLoginButton
            buttonText="Google Login"
            onSuccess={handleLogin}
            onFailure={() => setError('Server error')}
          />
        )}
      </CalendarForm>
    </FormWrapper>
  );
};

const FormWrapper = styled.div`
  ${FormLabel} {
    display: block;
  }
  ${FormInput} {
    margin-bottom: 8px;
  }
`;

const CalendarForm = styled.form`
  display: flex;
  min-width: 250px;
  justify-content: center;
  flex-wrap: wrap;
`;

const LoggedInParagraph = styled.p`
  margin-bottom: 10px;
  text-align: center;
`;
