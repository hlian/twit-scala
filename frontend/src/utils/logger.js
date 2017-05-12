import { isDevelop } from 'app/config';
import { identity } from 'app/utils';

/* eslint-disable no-console */
let logger = {
  dir: identity,
  info: identity,
  warn: identity,
  group: identity,
  groupEnd: identity,
  error: console.error,
};

if (isDevelop) {
  logger = {
    dir: console.dir,
    group: console.groupCollapsed,
    groupEnd: console.groupEnd,
    info: console.info,
    warn: console.warn,
    error: console.error,
  };
}
/* eslint-enable no-console */

module.exports = logger;
