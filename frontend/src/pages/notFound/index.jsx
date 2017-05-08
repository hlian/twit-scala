import React from 'react';
import styled from 'styled-components';

import t from 'i18n';
import { colors } from 'app/styles/variables';
import { sansFont } from 'app/styles/fonts';

const Header = styled.div`
  ${sansFont(800)}
  font-size: 32px;
  color: ${colors.secondary};
`;

const NotFoundComponent = () => <Header> {t('notFound')} </Header>;

export default NotFoundComponent;
