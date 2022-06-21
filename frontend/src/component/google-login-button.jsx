import React from 'react';

export const GoogleLoginButton = ({ buttonText, onSuccess }) => {
  const handleClick = (event) => {
    event.preventDefault();
    console.log(`Clicked on: ${buttonText}`);
    onSuccess();
  };

  return (
    <button className="login-btn" onClick={handleClick}>
      {buttonText}
    </button>
  );
};
