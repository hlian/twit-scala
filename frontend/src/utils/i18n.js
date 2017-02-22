import i18n from 'i18n-js';

import enLocale from 'app/locales/en';

i18n.translations.en = enLocale;
i18n.locale = 'en';

const translate = (string, options) => i18n.t(string, options);

export default translate;
