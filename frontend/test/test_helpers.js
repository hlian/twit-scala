process.env.NODE_ENV = 'test'

const chai = require('chai')
const sinon = require('sinon')
const sinonChai = require('sinon-chai')
const chaiEnzyme = require('chai-enzyme')
const jsdom = require('jsdom').jsdom

chai.use(sinonChai)
chai.use(chaiEnzyme())

global.expect = chai.expect
global.sinon = sinon

global.document = jsdom('')
global.window = document.defaultView
Object.keys(document.defaultView).forEach((property) => {
  if (typeof global[property] === 'undefined') {
    global[property] = document.defaultView[property]
  }
})

global.navigator = {
  userAgent: 'node.js'
}

documentRef = document
