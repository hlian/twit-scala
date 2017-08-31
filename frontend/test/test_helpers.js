process.env.NODE_ENV = 'test';

const chai = require('chai');
const sinon = require('sinon');
const sinonChai = require('sinon-chai');
const chaiEnzyme = require('chai-enzyme');
const { JSDOM } = require('jsdom');

chai.use(sinonChai);
chai.use(chaiEnzyme());

global.expect = chai.expect;
global.sinon = sinon;

global.document = (new JSDOM('<!doctype html>')).window.document;
global.window = document.defaultView;
Object.keys(document.defaultView).forEach((property) => {
  if (typeof global[property] === 'undefined') {
    global[property] = document.defaultView[property];
  }
});

global.navigator = {
  userAgent: 'node.js',
};
