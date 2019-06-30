const mongoose = require('mongoose')
const Paging = require('../repositories/paging/paging')
const Schema = mongoose.Schema

var responseSchema = new Schema({
  httpCode: { type: Number },
  responseMock: { type: Object }
})
var conditionBody = new Schema({
  when: { type: String },
  filledBy: { type: Object },
  throw: responseSchema
})

var bodyProperties = new Schema({
  name: { type: String },
  isRequired: { type: Boolean, default: false },
  type: { type: String },
  failMock: responseSchema,
  conditions: [conditionBody]
})

var headerSchema = new Schema({
  name: { type: String },
  isRequired: { type: Boolean, default: false },
  failMock: responseSchema
})

var mockupSchema = new Schema({
  _name: { type: String },
  _desc: { type: String },
  _path: { type: String },
  _method: { type: String },
  _header: [headerSchema],
  _defaultSuccess: { type: Object },
  _body: [bodyProperties],
  _payloadExmaple: { type: Object }
})

const model = mongoose.model('users', mockupSchema)

model.searchable = {
  '_id': '_id',
  'name': '_name',
  'path': '_path'
}
model.aliasPaging = {
  '_id': 'id',
  '_name': 'name',
  '_path': 'path'
}
model.pagination = function (req, callback) {
  let pagination = new Paging()
  pagination.select = '_id _name _desc _path'
  pagination.model = model
  pagination.getPagination(req, (data) => {
    callback(data)
  })
}

module.exports = model
