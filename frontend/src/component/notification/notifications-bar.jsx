import React from 'react';
import { NotificationsSearchForm } from './notifications-search-form';
import { NotificationsTextArea } from './notifications-text-area';

export const NotificationsBar = () => {
  return (
    <div className="notifications-bar">
      <NotificationsSearchForm />
      <NotificationsTextArea />
    </div>
  );
};
