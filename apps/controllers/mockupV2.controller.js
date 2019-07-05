var express = require('express')
var router = express.Router()
var mockup = require('./../repositories/mockupv2.repository')
var requestHelper = require('./../helper/request.helper')
var mockUpHelper = require('./../helper/mockup.helper')


/**
 * {{path}}?size=1&page=1&sort=_id&query=path:v2
 * for sorting - mean desc
 */
router.all('/', (req, res, next) => {
  mockup.pagination(req, (data) => {
    res.send(data)
  })
})

router.get('/desc', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    mockup.findOne({
      '_path': transformPath
    }).then(collection => {
      return res.status(200).send(collection)
    })
  }
})

router.all('/mocks', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    console.log(transformPath)
    mockup.findOne({
      '_path': transformPath,
      '_method': req.method.toLowerCase()
    }).then(async collection => {
      if (collection === null) {
        return res.send('mockup not found')
      }
      if (collection._header === null || collection._header.length === 0) {
        let header = await mockUpHelper.transformHeader(collection._header, req.headers)
        if (header != null) {
          return res.status(header.httpCode).send(header.result)
        }
      }
      if (collection._body === null || collection._body.isEmpty()) {
        let body = await mockUpHelper.transformBody(collection._body, req.body)
        if (body !== null) {
          return res.status(body.httpCode).send(body.result)
        }
      }
      return res.status(collection._defaultResponse.throw.httpCode).send(collection._defaultResponse.throw.result)
    }).catch(excExtract => {
      next(excExtract)
    })
  } else {
    res.body('ok')
  }
})
//
module.exports = router
