var express = require('express')
var router = express.Router()
var requestHelper = require('./../helper/request.helper')
var mocks = require('./../services/mockup.services')
const mockup = require('./../repositories/mockupv2.repository')
var mockUpHelper = require('./../helper/mockup.helper')
let responseCode = require('./../helper/response')
/**
 * {{path}}?size=1&page=1&sort=_id&query=path:v2
 * for sorting - mean desc
 */
router.all('/', (req, res, next) => {
  mocks.pagination(req, (err, data) => {
    console.log(err)
    if (err !== null) {
      return res.responseFail(responseCode.type.INTERNAL_SERVER_ERROR,err.message)
    }
    return res.responseOk(responseCode.type.SUCCESS,data,'success')
  })
})
router.post('/mocks/register', (req, res, next) => {
  mocks.store(req.body, (err,validation, data) => {
    if (err !== null) return next(err)
    if(validation!==null)return res.responseFail(responseCode.type.VALIDATION,validation)
    return res.responseOk(responseCode.type.SUCCESS,data,'success')
  })
})
router.get('/mocks/:id', (req, res, next) => {
  mocks.show(req.params.id, (err, data) => {
    if (err != null)  return res.responseFail(responseCode.type.INTERNAL_SERVER_ERROR,err.message)
    if (data == null) return res.responseFail('mocks not found', responseCode.type.DATA_NOT_FOUND)
    return res.responseOk(responseCode.type.SUCCESS,data,'success')
  })
})
router.post('/mocks/:id/update', (req, res, next) => {
  mocks.update(req.params.id, req.body, (err, data) => {
    if (err != null) return next(err)
    return res.responseOk(responseCode.type.SUCCESS,data,'success')
  })
})

router.get('/mocks/:id/delete', (req, res, next) => {
  mocks.delete(req.params.id, (err, data) => {
    if (err != null) return next(err)
    return res.responseOk(responseCode.type.SUCCESS,data,'success')
  })
})

router.get('/desc', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    mocks.desc(transformPath, (err, data) => {
      if (err !== null) {
        return next(err)
      }
      return res.responseOk(responseCode.type.SUCCESS,data,'success')
    })
  } else {
     return res.responseFail(responseCode.type.DATA_NOT_FOUND,data,'mocks not found')
  }
})

router.all('/mocks', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    mocks.mock(transformPath, req.method.toLowerCase(), req, async (err, httpCode, data) => {
      if (err !== null) return next(err)
      //special case for mocks
      return res.status(httpCode).send(data)
    })
  } else {
    return res.responseFail(responseCode.type.DATA_NOT_FOUND,data,'mocks not found')
  }
})

module.exports = router
