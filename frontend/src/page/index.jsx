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
  height: 100vh;
  justify-content: space-between;
  padding: 15px;
  gap: 0 15px;
`;

const CalendarBarWrapper = styled.div`
  padding: 30px 15px;
  background-color: var(--gray);
`;
