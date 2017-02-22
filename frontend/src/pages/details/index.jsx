import React from 'react';
import styled from 'styled-components';

import t from 'i18n';
import { colors } from 'app/styles/variables';
import { sansFont } from 'app/styles/fonts';
import NavigationLink from 'app/components/navigationLink';
import GlobeIcon from 'app/assets/icons/globe.svg';

const DetailsContainer = styled.div`
  p { margin-top: 10px; }
  hr { margin: 20px 0; }
`;

const Header = styled.h1`
  ${sansFont(800)}
  font-size: 32px;
  color: ${colors.primary};
`;

const Globe = styled(GlobeIcon)`
  fill: ${colors.secondary};
  width: 200px;
  height: 200px;
  display: block;
`;

const DetailsComponent = () =>
  <DetailsContainer>
    <Header> {t('home.title')} </Header>
    <p> {t('home.seeReadme')} </p>
    <hr />
    <div>
      <Globe />
      {t('home.internationalizedString')}
    </div>
    <p>
      <NavigationLink to="/demo">{t('home.demoAppLink')}</NavigationLink>
    </p>
  </DetailsContainer>;

export default DetailsComponent;
