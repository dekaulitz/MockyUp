const repository = require('./../repositories/mockupv2.repository')
const baseValidation = require('./base.validation')
let mocksValidation = function () {
  baseValidation.call(this)
}
mocksValidation.prototype = Object.create(baseValidation.prototype)
/**
 * @description base validation before insert
 * @param properties
 * @param callback
 * @returns {Promise<*>}
 */
mocksValidation.prototype.beforeInsert = async function (properties, callback) {
  if (this.isEmpty(properties._name)) return callback(null, '_name is required')
  if (this.isEmpty(properties._path)) return callback(null, '_path is required')
  if (this.isEmpty(properties._defaultResponse)) return callback(null, '_defaultResponse is required')
  if (this.isEmpty(properties._method)) return callback(null, '_method is required')
  let isExist = await repository.findOne({ _path: properties._path, _method: properties._method })
  if (isExist != null) return callback(null, 'data already exist')

  return callback(null, null)
}

module.exports = mocksValidation
