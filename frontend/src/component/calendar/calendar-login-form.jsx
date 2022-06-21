import React, { useState } from 'react';
import { GoogleLoginButton } from '../google-login-button';

export const CalendarLoginForm = () => {
  const [error, setError] = useState();
  const [calendarId, setCalendarId] = useState();

  const handleLogin = () => {
    console.log(`Form submitted: calendarId=${calendarId}`);
  };

  return (
    <div className="calendar-form-wrapper">
      <form className="calendar-form" onSubmit={handleLogin}>
        {error ? (
          <div className="error-block" onClick={() => setError('')}>
            {error}
          </div>
        ) : null}
        <input
          name="calendar__id"
          type="text"
          onChange={(e) => setCalendarId(e.target.value.trim())}
        />
        <GoogleLoginButton
          buttonText="Login with Google"
          onSuccess={handleLogin}
          onFailure={() => setError('Server error')}
        />
      </form>
    </div>
  );
};
