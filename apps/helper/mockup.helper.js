const _ = require('lodash')
const helper = require('./request.helper')

const IS_ARRAY_OBJECT = 'arrayobject'
/**
 * @description its required fro extracting and validating request header with header scheme from database
 * @param collections
 * @param requestheader
 * @returns {Promise<*>}
 */
module.exports.transformHeader = async (collections, requestheader) => {
  let res = null
  _.forEach(collections, (collection, index) => {
      let element = collection._doc
      let stop = false
      if (element.isRequired) {
        if (requestheader[element.name.toLowerCase()] === null || requestheader[element.name.toLowerCase()] === undefined) {
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

/**
 * @description its required for extracting and validating the body json with body scheme from database
 * @param body
 * @param requestBody
 * @returns {Promise<*>}
 */
async function bodyExtraction (body, requestBody) {
  let result = null
  _.forEach(body.values, (collection) => {
    let value=collection._doc
    let stop = false
    if (value.isRequired) {
      if (requestBody[value.name] === null || requestBody[value.name] === undefined) {
        stop = true
        result = value.throw
        return false
      }
    }
    if (value.conditions != null || value.conditions !== undefined) {
      _.forEach(value.conditions, (collection) => {
        let condition=collection._doc
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

/**
 * @description its required for transfrom body and validating check is body is array of object or no using same validation function bodyExtraction(collections,body)
 * @param collections
 * @param requestBody
 * @returns {Promise<*>}
 */
module.exports.transformBody = async (collections, requestBody) => {
  let res = null
  if (collections.isRequired === true && (requestBody === undefined || helper.isEmptyObject(requestBody))) {
    res = collections.throw
  } else {
    if (collections.type.toLowerCase() === IS_ARRAY_OBJECT) {
      for (let i = 0; i < requestBody.length; i++) {
        res = await bodyExtraction(collections, requestBody[i])
        if (res !== null) {
          break
        }
      }
    } else {
      res = await bodyExtraction(collections, requestBody)
    }
  }
  return res
}
