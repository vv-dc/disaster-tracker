import React, { useState } from 'react';
import styled from 'styled-components';
import {
  BaseButton,
  FormColumn,
  FormInput,
  FormLabel,
} from '../styled/control';
import { formatKebabWithTZ } from '../../lib/date.utils';

export const NotificationsSearchForm = ({ formCallback }) => {
  const [timeMin, setTimeMin] = useState('');
  const [timeMax, setTimeMax] = useState('');
  const [calendarId, setCalendarId] = useState('');

  const handleSearch = async (event) => {
    event.preventDefault();
    const searchDto = prepareSearchDto();
    formCallback(searchDto);
  };

  const prepareSearchDto = () => {
    return {
      calendarId,
      timeMin: formatKebabWithTZ(timeMin),
      timeMax: formatKebabWithTZ(timeMax),
    };
  };

  return (
    <SearchFormWrapper>
      <SearchForm className="search-form" onSubmit={handleSearch}>
        <FormColumnsWrapper>
          <FormColumn>
            <FormLabel htmlFor="search-form__calendar-id">
              Calendar ID
            </FormLabel>
            <FormInput
              id="search-form__calendar-id"
              name="time-min"
              type="text"
              onChange={(e) => setCalendarId(e.target.value.trim())}
              value={calendarId}
              required
            />
          </FormColumn>
          <FormColumn>
            <FormLabel htmlFor="search-form__time-min">Min</FormLabel>
            <FormInput
              id="search-form__time-min"
              name="time-min"
              type="datetime-local"
              onChange={(e) => setTimeMin(e.target.value.trim())}
              value={timeMin}
              required
            />
          </FormColumn>
          <FormColumn>
            <FormLabel htmlFor="search-form__time-max">Max</FormLabel>
            <FormInput
              id="search-form__time-max"
              name="time-max"
              type="datetime-local"
              onChange={(e) => setTimeMax(e.target.value.trim())}
              value={timeMax}
              required
            />
          </FormColumn>
        </FormColumnsWrapper>
        <SearchButton>Search</SearchButton>
      </SearchForm>
    </SearchFormWrapper>
  );
};

const SearchFormWrapper = styled.div`
  margin: 0 auto;
  ${FormInput} {
    width: 130px;
  }
  ${FormLabel} {
    display: block;
    margin-right: 6px;
  }
`;

const FormColumnsWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0 20px;
  ${FormInput} {
    width: 200px;
  }
`;

const SearchButton = styled(BaseButton)`
  background-color: var(--gray);
  color: var(--black);
  padding: 15px 8px;
  width: 100%;
`;

const SearchForm = styled.form`
  display: flex;
  justify-content: center;
  gap: 10px 15px;
  flex-wrap: wrap;
`;
