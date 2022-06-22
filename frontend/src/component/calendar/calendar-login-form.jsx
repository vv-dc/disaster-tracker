import React, { useState } from 'react';
import styled from 'styled-components';
import { GoogleLoginButton } from '../google-login/google-login-button';
import { FormColumn, FormInput, FormLabel } from '../styled/control';

export const CalendarLoginForm = () => {
  const [error, setError] = useState();
  const [calendarId, setCalendarId] = useState();

  const handleLogin = () => {
    console.log(`Form submitted: calendarId=${calendarId}`);
  };

  return (
    <FormWrapper>
      <CalendarForm onSubmit={handleLogin}>
        {error ? (
          <div className="error-block" onClick={() => setError('')}>
            {error}
          </div>
        ) : null}
        <FormColumn>
          <FormLabel htmlFor="calendar__id">Calendar ID</FormLabel>
          <FormInput
            name="calendar__id"
            type="text"
            onChange={(e) => setCalendarId(e.target.value.trim())}
          />
        </FormColumn>
        <GoogleLoginButton
          buttonText="Google Login"
          onSuccess={handleLogin}
          onFailure={() => setError('Server error')}
        />
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
  justify-content: center;
  flex-wrap: wrap;
`;
