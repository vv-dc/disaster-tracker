import styled from 'styled-components';

export const FormInput = styled.input`
  padding: 3px 6px;
  background-color: var(--white);
  line-height: 20px;
  height: 30px;
  border: 1px solid var(--gray);
  &:focus {
    outline: 1px solid var(--gray-dark);
  }
`;

export const FormLabel = styled.label`
  font-weight: 600;
  margin-bottom: 7px;
`;

export const FormColumn = styled.div`
  text-align: center;
`;

export const BaseButton = styled.button`
  padding: 3px 16px;
  border: none;
  border-radius: 6px;
  vertical-align: baseline;
  cursor: pointer;
`;
