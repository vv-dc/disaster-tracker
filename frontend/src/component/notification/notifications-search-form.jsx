import React, { useState } from 'react';
import styled from 'styled-components';
import {
  BaseButton,
  FormColumn,
  FormInput,
  FormLabel,
} from '../styled/control';

export const NotificationsSearchForm = () => {
  const [timeMin, setTimeMin] = useState('');
  const [timeMax, setTimeMax] = useState('');
  const [calendarId /*setCalendarId*/] = useState('');

  const handleSearch = (event) => {
    event.preventDefault();
    console.dir({
      timeMin,
      timeMax,
      calendarId,
    });
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
              onChange={(e) => setTimeMin(e.target.value.trim())}
              value={timeMin}
            />
          </FormColumn>
          <FormColumn>
            <FormLabel htmlFor="search-form__time-min">Min</FormLabel>
            <FormInput
              id="search-form__time-min"
              name="time-min"
              type="date"
              onChange={(e) => setTimeMin(e.target.value.trim())}
              value={timeMin}
            />
          </FormColumn>
          <FormColumn>
            <FormLabel htmlFor="search-form__time-max">Max</FormLabel>
            <FormInput
              id="search-form__time-max"
              name="time-max"
              type="date"
              onChange={(e) => setTimeMax(e.target.value.trim())}
              value={timeMax}
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
  gap: 0 20px;
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
