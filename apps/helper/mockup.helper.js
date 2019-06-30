const _ = require('lodash')
const IS_OBJECT = 'object'
const IS_ARRAY_OBJECT = 'arrayobject'

module.exports.transformHeader = async (collections, requestheader) => {
  let res = null
  _.forEach(collections, (element, index) => {
      let stop = false
      if (element.isRequired) {
        if (requestheader[element.name] === null || requestheader[element.name] === undefined) {
          stop = true
          res = element.throw
          return false
        }
        if (element.conditions != null || element.conditions !== undefined) {
          _.forEach(element.conditions, (condition) => {
            if ((condition.when !== null) && (condition.when !== undefined)) {
              if (requestheader[element.name] === condition.when.filledBy) {
                stop = true
                res = condition.when
                return false
              }
            }
          })
        }
      }
      if (stop) return false
    }
  )
  return res
}

async function bodyExtraction (body, requestBody) {
  let result = null
  _.forEach(body.values, (value) => {
    let stop = false
    if (value.isRequired) {
      if (requestBody[value.name] === null || requestBody[value.name] === undefined) {
        stop = true
        result = value.throw
        return false
      }
    }
    if (value.conditions != null || value.conditions !== undefined) {

      _.forEach(value.conditions, (condition) => {

        if ((condition.when !== null) && (condition.when !== undefined)) {
          if (requestBody[value.name] === condition.when.filledBy) {
            stop = true
            result = condition.when
            return false
          }
        }
      })
    }
    if (stop) return false
  })
  return result
}

module.exports.transformBody = async (collections, requestBody) => {
  let res = null
  if (collections.type.toLocaleLowerCase() === IS_ARRAY_OBJECT) {
    _.forEach(requestBody, (body) => {
      res = bodyExtraction(collections, body)
      if (res !== null) {
        return false
      }
    })
  } else {
    res = await bodyExtraction(collections, requestBody)
  }
  return res
}

