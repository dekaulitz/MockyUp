const baseValidation = function () {
  this.errValidation = {}
}
baseValidation.prototype.isEmpty = function (property) {
  return property === undefined || property === null
}
module.exports = baseValidation
