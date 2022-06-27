import React from 'react';

export const NotificationsTextArea = ({ notifications }) => {
  if (!notifications?.length) return null;
  return (
    <textarea
      key={notifications}
      readOnly
      defaultValue={JSON.stringify(notifications, null, 4)}
    />
  );
};
