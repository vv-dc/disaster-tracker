import React from 'react';
import styled from 'styled-components';
import { NotificationsSearchForm } from './notifications-search-form';
import { NotificationsTextArea } from './notifications-text-area';

export const NotificationsBar = () => {
  return (
    <NotificationsBarWrapper>
      <NotificationsSearchForm />
      <NotificationTextAreaWrapper>
        <NotificationsTextArea
          notifications={[
            { id: 1, description: '123' },
            { id: 2, description: '456' },
            { id: 3, description: '789' },
          ]}
        />
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
    width: inherit;
    box-sizing: border-box;
    height: 100%;
  }
`;
