const mongoose = require('mongoose')
const Paging = require('../repositories/paging/paging')
const Schema = mongoose.Schema

var response = new Schema({
  httpCode: { type: Number },
  result: { type: Object }
})
var condition = new Schema({
  filledBy: { type: Object },
  response
})

var headerSchema = new Schema({
  name: { type: String },
  type: { type: String },
  isRequired: { type: Boolean },
  throw: { type: Object },
  conditions: [condition]
})
var body = new Schema({
  name: { type: String },
  type: { type: String },
  isRequired: { type: Boolean },
  throw: { type: Object },
  conditions: [condition]
})

var mockUpv2 = new Schema({
  _name: { type: String },
  _desc: { type: String },
  _path: { type: String },
  _method: { type: String },
  _header: [headerSchema],
  _body: {
    type: { type: String },
    consumens: { type: String },
    values: [body],
    isRequired: { type: Boolean },
    throw: { type: Object },
  },
  _defaultResponse: {
    throw: { type: Object }
  }
})
const model = mongoose.model('mockup_v2', mockUpv2)
module.exports = model
