const _ = require('lodash')

const baseValidation = function () {
  this.errValidation = {}
}
baseValidation.prototype.isEmpty = function (property) {
  if (property === undefined || property === null) return true
  return false
}

module.exports = baseValidation
