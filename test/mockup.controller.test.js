process.env.NODE_ENV = 'test'

let mongoose = require('mongoose')
let MockUp = require('../apps/controllers/mockup.controller')

//Require the dev-dependencies
let chai = require('chai')
let chaiHttp = require('chai-http')
let server = require('../app')
let should = chai.should()
let expect = require('chai').expect

chai.use(chaiHttp)
/*
  * Test the /GET route
  */
describe('/GET mocks', () => {
  it('it should GET all the books', (done) => {
    chai.request(server)
      .get('/mocks')
      .end((err, res) => {
        res.should.have.status(200)
        done()
      })
  })
  it('it should Describe the path', (done) => {
    chai.request(server)
      .get('/desc?path=/api/v2/users')
      .end((err, res) => {
        res.should.have.status(200)
        expect(res).contain({ "_id": '5d131f39b7411f344e33bd0b' })
        done()
      })
  })
})
