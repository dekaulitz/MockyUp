var _ = require('lodash')
var filter = {}

/**
 * @description this function is required for replace the *something* to *** its for searching url from database
 * @param routePath
 * @returns {string}
 */
filter.transformPath = function (routePath) {
  let urlArray = routePath.split('')
  let startRegext = false
  let newPath = ''
  _.forEach(urlArray, (element, index) => {
    if (!startRegext) {
      newPath += element
    }
    if (element === '*') {
      newPath += '*'
      startRegext = !startRegext
    }
  })
  return newPath
}
/**
 * @description this function is a prototype from object for checking object is empty
 * @returns {boolean}
 */
filter.isEmptyObject = function(object) {
  return  Object.keys(object).length === 0;
}
module.exports = filter
