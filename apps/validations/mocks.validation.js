const repository = require('./../repositories/mockupv2.repository')
const _ = require('lodash')
const helper = require('./../helper/request.helper')
const response = require('./../helper/response')
const baseValidation = require('./base.validation')
const validation = require('../error_handler/validation.handler')
let mocksValidation = function () {
  baseValidation.call(this)
}
mocksValidation.prototype = Object.create(baseValidation.prototype)

mocksValidation.prototype.beforeInsert = async function (properties, callback) {
  if (this.isEmpty(properties._name)) {
    return callback('_name is required')
  }
  if (this.isEmpty(properties._path)) {
    return callback('_path is required')
  }

  if (this.isEmpty(properties._defaultResponse)) {
    return callback('_defaultResponse is required')
  }

  if (this.isEmpty(properties._method)) {
    return callback('_method is required')
  }

  let isExist = await repository.findOne({ _path: properties._path, _method: properties._method })
  if (isExist != null) return callback('data already exist')

  return callback(null)
}

module.exports = mocksValidation
