const mongoose = require('mongoose')
const Paging = require('../repositories/paging/paging')
const Schema = mongoose.Schema

let condition = new Schema({
  _id: { id: false },
  when: {
    filledBy: { type: Object },
    httpCode: { type: Number },
    result: { type: Object }
  }
}, { strict: true })

let headerSchema = new Schema({
  _id: { id: false },
  name: { type: String },
  type: { type: String },
  isRequired: { type: Boolean },
  throw: { type: Object },
  conditions: [condition]
})
let bodyDetail = new Schema({
  _id: { id: false },
  name: { type: String },
  type: { type: String },
  isRequired: { type: Boolean },
  throw: { type: Object },
  conditions: [condition]
}, { strict: true })
let bodyPayload = new Schema({
  _id: { id: false },
  type: { type: String },
  consumes: { type: String },
  values: [bodyDetail],
  isRequired: { type: Boolean },
  throw: { type: Object }
}, { strict: true })

let mockUpv2 = new Schema({
  _name: { type: String },
  _desc: { type: String },
  _path: { type: String },
  _method: { type: String },
  _header: [headerSchema],
  _body: bodyPayload,
  _defaultResponse: {
    throw: { type: Object }
  }
}, { strict: true })
const mocks = mongoose.model('mockup_v2', mockUpv2)

mocks.searchable = {
  '_id': '_id',
  'name': '_name',
  'path': '_path'
}
mocks.aliasPaging = {
  '_id': 'id',
  '_name': 'name',
  '_path': 'path'
}
mocks.pagination = function (req, callback) {
  let pagination = new Paging()
  pagination.select = '_id _name _desc _path'
  pagination.model = mocks
  pagination.getPagination(req, (err, data) => {
    callback(err, data)
  })
}
module.exports = mocks
