import React from 'react';

export const NotificationsTextArea = ({ notifications }) => {
  return (
    <div className="notifications">
      <textarea className="notifications-inner" cols="60">
        {JSON.stringify(notifications, null, 4)}
      </textarea>
    </div>
  );
};
