const repository = require('./../repositories/mockupv2.repository')
var mockUpHelper = require('./../helper/mockup.helper')
var mocks = {}
/**
 * @description listisng all data from repository
 * @param callback
 */
mocks.lists = (callback) => {
  repository.find((err, docs) => {
    callback(err, docs)
  })
}
/**
 * @description paginate the repository
 * @param paging
 * @param callback
 */
mocks.pagination = (paging, callback) => {
  repository.pagination(paging, (err, data) => {
    callback(err, data)
  })
}
/**
 * @description describe the mock
 * @param path
 * @param callback
 */
mocks.desc = (path, callback) => {
  repository.findOne({
    '_path': path
  }, (err, data) => {
    callback(err, data)
  })
}
/**
 * @description load mock by path and extract the property
 * @param path
 * @param method
 * @param req
 * @param callback
 */
mocks.mock = (path, method, req, callback) => {
  repository.findOne({
    '_path': path,
    '_method': method
  }).then(async (data) => {
    if (data === null || data === undefined) {
      return callback(null, 200, 'mockup not found')

    } else {
      if (data._header !== null || data._header.length !== 0) {
        let header = await mockUpHelper.transformHeader(data._header, req.headers)
        if (header != null) {
          return callback(null, header.httpCode, header.result)
        }
      }
      if (data._body !== null || !requestHelper.isEmptyObject(data._body)) {
        let body = await mockUpHelper.transformBody(data._body, req.body)
        if (body !== null) {
          return callback(null, body.httpCode, body.result)
        }
      }
      return callback(null, data._defaultResponse.throw.httpCode, data._defaultResponse.throw.result)
    }
  }).catch(reason => {
    return callback(reason, null, null)
  })
}

module.exports = mocks
