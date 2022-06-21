import React from 'react';

export const NotificationsTextArea = ({ notifications }) => {
  if (!notifications?.length) return null;
  return <textarea defaultValue={JSON.stringify(notifications, null, 4)} />;
};
