import React, { useState } from 'react';

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
    <div className="search-form-wrapper">
      <form className="search-form" onSubmit={handleSearch}>
        <input
          id="search-form__time-min"
          name="time-min"
          onChange={(e) => setTimeMin(e.target.value.trim())}
          value={timeMin}
        />
        <input
          id="search-form__time-max"
          name="time-max"
          onChange={(e) => setTimeMax(e.target.value.trim())}
          value={timeMax}
        />
        <button className="search-btn">Search</button>
      </form>
    </div>
  );
};
