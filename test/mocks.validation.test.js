const mockValidation = require('../apps/validations/mocks.validation')
const assert = require('chai').assert
const expect = require('chai').expect
const response = require('./../apps/helper/response')
let envPath = process.env.NODE_ENV === undefined ? '.env.test' : '.env.' + process.env.NODE_ENV
let config = require('./../configuration')
config.loadConfiguration(envPath)
const repository = require('./../apps/repositories/mockupv2.repository')
const db = config.loadDatabase(config).connection
db.on('error', console.error.bind(console, 'connection error:'))
db.once('open', function () {
  console.log('db connected')
})

let give = {}

let validation = new mockValidation()
describe('mockup validation test', (done) => {

  beforeEach(() => {
    give = {
      _name: 'asdasd',
      _path: '/v1/api/coba',
      _method: 'POST',
      _defaultResponse: { statusCode: 123, message: '123' }
    }
  })

  it('should be error  because validation _name', function (done) {
    give._name = null
    validation.beforeInsert(give, (errValidation) => {
      assert.equal('_name is required', errValidation)
      done()
    }).catch(reason => {
      console.log(reason)
      done()
    })
  })

  it('should be error  because validation _path ', function (done) {
    give._path = null
    validation.beforeInsert(give, (errValidation) => {
      assert.equal('_path is required', errValidation)
      done()
    }).catch(reason => {
      console.log(reason)
    })

  })
  it('should be error  because validation _method ', function (done) {
    give._method = null
    validation.beforeInsert(give, (errValidation) => {
      assert.equal('_method is required', errValidation)
      done()
    }).catch(reason => {
      console.log(reason)
      done()
    })
  })
  it('should be data already exist', function (done) {
    give._method = 'post'
    give._path = '/v1/api/users'
    validation.beforeInsert(give, (errValidation) => {
      console.log(errValidation)
      assert.equal('data already exist', errValidation)
      done()
    }).catch(reason => {
      console.log(reason)
      done()
    })
  })
})
