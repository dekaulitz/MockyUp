let mockupHelper = require('../apps/services/mockup.services')
var mockuprepo = require('../apps/repositories/mockupv2.repository')
const assert = require('chai').assert
let envPath = process.env.NODE_ENV === undefined ? '.env.test' : '.env.' + process.env.NODE_ENV
var config = require('./../configuration')
config.loadConfiguration(envPath)

before('load database connection', () => {
  const db = config.loadDatabase(config).connection
  db.on('error', console.error.bind(console, 'connection error:'))
  db.once('open', function () {
    console.log('db connected')
  })
})

describe('test mockup service', () => {
  it('test mockup lists', function () {
    mockupHelper.lists((err, data) => {
      assert.isNotEmpty(data)
    })
  })

})
