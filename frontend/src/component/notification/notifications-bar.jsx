import React, { useState } from 'react';
import styled from 'styled-components';
import { NotificationsSearchForm } from './notifications-search-form';
import { NotificationsTextArea } from './notifications-text-area';

export const NotificationsBar = () => {
  const [notifications, setNotifications] = useState([]);

  const handleNewNotifications = async (notifications) => {
    setNotifications((prev) => {
      return [...prev, ...notifications];
    });
  };

  return (
    <NotificationsBarWrapper>
      <NotificationsSearchForm formCallback={handleNewNotifications} />
      <NotificationTextAreaWrapper>
        <NotificationsTextArea notifications={notifications} />
      </NotificationTextAreaWrapper>
    </NotificationsBarWrapper>
  );
};

const NotificationsBarWrapper = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  flex-direction: column;
  gap: 12px 0;
`;

const NotificationTextAreaWrapper = styled.div`
  width: 100%;
  height: 100%;
  textarea {
    display: block;
    resize: none;
    width: inherit;
    box-sizing: border-box;
    height: 100%;
  }
`;
