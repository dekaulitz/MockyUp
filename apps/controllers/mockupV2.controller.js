var express = require('express')
var router = express.Router()
var requestHelper = require('./../helper/request.helper')
var mocks = require('./../services/mockup.services')
const mockup = require('./../repositories/mockupv2.repository')
var mockUpHelper = require('./../helper/mockup.helper')
/**
 * {{path}}?size=1&page=1&sort=_id&query=path:v2
 * for sorting - mean desc
 */
router.all('/', (req, res, next) => {
  mocks.pagination(req, (err, data) => {
    if (err !== null) {
      return res.status(500).send(err)
    }
    return res.status(200).send(data)
  })
})

router.get('/desc', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    mocks.desc(transformPath, (err, data) => {
      if (err !== null) {
        return res.status(500).send(err)
      }
      return res.status(200).send(data)
    })
  } else {
    return res.status(404).send('data not found')
  }
})

router.all('/mocks', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    mocks.mock(transformPath, req.method.toLowerCase(), req, async (err, httpCode, data) => {
      if (err !== null) return next(err)
      return res.status(httpCode).send(data)
    })
  }
})

module.exports = router
