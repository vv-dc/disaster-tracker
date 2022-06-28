import React from 'react';

export const NotificationsTextArea = ({ notifications }) => {
  if (!notifications?.length) return null;
  return (
    <textarea readOnly defaultValue={JSON.stringify(notifications, null, 4)} />
  );
};
