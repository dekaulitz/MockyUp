var _ = require('lodash')
var filter = {}

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

module.exports = filter
