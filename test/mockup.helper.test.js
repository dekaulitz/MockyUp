let mockupHelper = require('../apps/helper/mockup.helper')
const assert = require('chai').assert

//load resouce from json
let normalCollection = require('./resource.test').dummy
describe('mockup helper test', () => {
  /**
   * test header results
   */
  it('test hmac null', async function () {
    let requestHeader = {}
    let results = await mockupHelper.transformHeader(normalCollection._header, requestHeader)
    assert.equal(results.httpCode, 400)
  })
  it('test hmac exist but app id is null', async () => {
    let requestHeader = {
      'signature': '123123123'
    }
    let results = await mockupHelper.transformHeader(normalCollection._header, requestHeader)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 5001)
    assert.equal(results.result.message, 'app-id is required')
  })
  it('test hmac conditions  ', async function () {
    let requestHeader = {
      'signature': 'test_hmac'
    }
    let results = await mockupHelper.transformHeader(normalCollection._header, requestHeader)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 4001)
    assert.equal(results.result.message, 'invalid hmac')
  })
  it('test app-id conditions  ', async function () {
    let requestHeader = {
      'signature': '123.31223123123',
      'app-id': 'v.1.23'
    }
    let results = await mockupHelper.transformHeader(normalCollection._header, requestHeader)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 4002)
    assert.equal(results.result.message, 'invalid version')
  })

  /**
   * test body
   */

  it('test body null  ', async function () {
    let requestBody = {}
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 5000)
    assert.equal(results.result.message, 'internal server error')
    assert.equal(results.result.meta.timestamp, 123123123123)
    assert.equal(results.result.meta.status, false)
  })
  it('test body firstname exist but email null', async function () {
    let requestBody = {
      'firstName': 'ajo123'
    }
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 5003)
    assert.equal(results.result.message, 'email is required')
  })
  it('test body firstname conditions with fahmi', async function () {
    let requestBody = {
      'firstName': 'fahmi',
      'email': 'ajod@gmail.com'
    }
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 4002)
    assert.equal(results.result.message, 'user already exist')
  })
  it('test body firstname conditions with ajo', async function () {
    let requestBody = {
      'firstName': 'ajo',
      'email': 'ajod@gmail.com'
    }
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 4002)
    assert.equal(results.result.message, 'user already block')
    assert.equal(results.result.meta.debugId, '123-123-123')
  })
  it('test body email conditions with fahmi.sulaiman@ovo.id', async function () {
    let requestBody = {
      'firstName': 'ajo1',
      'email': 'fahmi.sulaiman@ovo.id'
    }
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 422)
    assert.equal(results.result.statusCode, 4002)
    assert.equal(results.result.message, 'email already registered')
  })
  it('test body email conditions with ajo@gmail.com', async function () {
    let requestBody = {
      'firstName': 'ajo1',
      'email': 'ajo@gmail.com'
    }
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 500)
    assert.equal(results.result.statusCode, 4002)
    assert.equal(results.result.message, 'email not valid')
  })
  it('test body addres with ', async function () {
    let requestBody = {
      'firstName': 'ajo1',
      'email': 'ajo2@gmail.com',
      'address': 'Menara kuningan'
    }
    let results = await mockupHelper.transformBody(normalCollection._body, requestBody)
    assert.equal(results.httpCode, 400)
    assert.equal(results.result.statusCode, 4001)
    assert.equal(results.result.message, 'location already blocked')
  })
})

