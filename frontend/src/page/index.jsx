import React from 'react';
import { CalendarLoginForm } from '../component/calendar/calendar-login-form';
import { NotificationsBar } from '../component/notification/notifications-bar';

export const IndexPage = () => {
  return (
    <div className="index-wrapper">
      <NotificationsBar />
      <CalendarLoginForm />
    </div>
  );
};
