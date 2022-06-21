import React from 'react';
import styled from 'styled-components';
import { CalendarLoginForm } from '../component/calendar/calendar-login-form';
import { NotificationsBar } from '../component/notification/notifications-bar';

export const IndexPage = () => {
  return (
    <IndexPageWrapper>
      <NotificationsBar />
      <CalendarBarWrapper>
        <CalendarLoginForm />
      </CalendarBarWrapper>
    </IndexPageWrapper>
  );
};

const IndexPageWrapper = styled.div`
  display: flex;
  height: calc(100vh - 50px);
  justify-content: space-between;
  padding: 15px;
  gap: 0 15px;
`;

const CalendarBarWrapper = styled.div`
  background-color: green;
`;
