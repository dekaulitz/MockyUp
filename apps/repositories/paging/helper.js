const entity = require('./entity')
const _ = require('lodash')
const helper = function () {
  entity.call(this)
}
helper.prototype = Object.create(entity)
Object.defineProperty(helper, 'model', {
  get: function () {
    return this.value
  },
  set: function (x) {
    this.value = x
  }
})
Object.defineProperty(helper, 'select', {
  get: function () {
    return this.value
  },
  set: function (x) {
    this.value = x
  }
})

helper.prototype.extractRequest = async function (req) {
  _.forEach(req.query, (value, key) => {
    if (key === 'sort') {
      if (this.model.aliasPaging[value]) {
        this.sorting = this.model.aliasPaging[value]
      }
    }
    if (key === 'q') {
      let query = value.split(':')
      if (this.model.searchable[query[0]]) {
        let obj = {}
        if (this.model.searchable[query[0]] === '_id') {
          obj[this.model.searchable[query[0]]] = query[1]
        } else {
          obj[this.model.searchable[query[0]]] = { $regex: new RegExp(query[1], 'i') }
        }
        this.query = obj
      }
    }
    if (this[key] && key !== 'query') {
      this[key] = value
    }
  })
  return this
}

module.exports = helper
