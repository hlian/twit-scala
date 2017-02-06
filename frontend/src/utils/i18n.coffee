i18n = require 'i18n-js'

i18n.translations.en = require '../locales/en'
i18n.locale = 'en'

module.exports = (string, options) ->
  i18n.t string, options
