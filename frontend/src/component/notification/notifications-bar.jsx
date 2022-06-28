import React, { useRef, useState } from 'react';
import styled from 'styled-components';
import { NotificationsSearchForm } from './notifications-search-form';
import { NotificationsTextArea } from './notifications-text-area';
import {
  getNotificationsStream,
  parseNotification,
} from '../../service/notification.service';
import { BaseButton } from '../styled/control';
import { ErrorBlock } from '../styled/error';

export const NotificationsBar = () => {
  const [integrity, setIntegrity] = useState(null);
  const integrityRef = useRef(integrity);

  const [error, setError] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [stream, setStream] = useState(null);

  const closeStream = () => {
    stream.close();
    setStream(null);
  };

  const updateIntegrity = (value) => {
    integrityRef.current = value;
    setIntegrity(value);
  };

  const addNotification = (notification) => {
    if (integrityRef.current !== notification.integrity) {
      updateIntegrity(notification.integrity);
      setNotifications([notification]);
    } else setNotifications((prev) => [...prev, notification]);
  };

  const handleEnableStream = (searchDto) => {
    if (stream !== null) {
      setError('Stream is already enabled!');
      return;
    }
    const newStream = getNotificationsStream(searchDto);
    addStreamListeners(newStream);
    setStream(newStream);
  };

  const addStreamListeners = (newStream) => {
    newStream.onmessage = (message) => {
      const notification = parseNotification(message);
      addNotification(notification);
    };
    newStream.onerror = () => {
      setError('Something went wrong...');
    };
  };

  return (
    <NotificationsBarWrapper>
      <NotificationsSearchForm formCallback={handleEnableStream} />
      {error !== null ? (
        <ErrorBlock onClick={() => setError(null)}>{error}</ErrorBlock>
      ) : null}
      {stream !== null ? (
        <BaseButton
          onClick={(event) => {
            event.preventDefault();
            closeStream();
          }}
        >
          Disconnect
        </BaseButton>
      ) : null}
      <NotificationTextAreaWrapper key={integrityRef.current}>
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

  ${BaseButton} {
    background-color: var(--gray);
  }
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
