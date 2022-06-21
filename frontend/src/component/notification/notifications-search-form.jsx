import React, { useState } from 'react';
import styled from 'styled-components';
import { BaseButton, FormInput } from '../styled/control';

export const NotificationsSearchForm = () => {
  const [timeMin, setTimeMin] = useState('');
  const [timeMax, setTimeMax] = useState('');

  const handleSearch = (event) => {
    event.preventDefault();
    console.dir({
      timeMin,
      timeMax,
    });
  };

  return (
    <SearchFormWrapper>
      <SearchForm className="search-form" onSubmit={handleSearch}>
        <FormInput
          id="search-form__time-min"
          name="time-min"
          onChange={(e) => setTimeMin(e.target.value.trim())}
          value={timeMin}
          placeholder="Min Date"
        />
        <FormInput
          id="search-form__time-max"
          name="time-max"
          onChange={(e) => setTimeMax(e.target.value.trim())}
          value={timeMax}
          placeholder="Max Date"
        />
        <SearchButton>Search</SearchButton>
      </SearchForm>
    </SearchFormWrapper>
  );
};

const SearchFormWrapper = styled.div`
  display: flex;
  margin: 0 auto;
  ${FormInput} {
    width: 100px;
    border: 1px solid var(--gray);
  }
  label {
    margin-right: 6px;
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
