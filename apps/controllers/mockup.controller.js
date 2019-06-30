var express = require('express')
var router = express.Router()
var mockup = require('./../repositories/mockup_repositories')
var requestHelper = require('./../helper/request.helper')

/**
 * {{path}}?size=1&page=1&sort=_id&query=path:v2
 * for sorting - mean desc
 */
router.all('/', (req, res, next) => {
  mockup.pagination(req, (data) => {
    res.send(data)
  })
})

router.all('/mock', (req, res, next) => {
  if (req.query.path != null) {
    var transformPath = requestHelper.transformPath(req.query.path)
    mockup.findOne({
      '_path': transformPath,
      '_method': req.method.toLowerCase()
    }).then(collection => {
      let validationFail = false
      if (collection == true) {
        console.log("collection 1")
        validationFail = true
        return res.send('mock not found')
      } else if (req.method.toLowerCase() !== collection._method.toLowerCase()) {
        console.log("collection 2")
        validationFail = true
        return res.send('invalid method')
      } else if (collection._header.length > 0) {
        console.log("collection 3")
        collection._header.forEach((element) => {
          if (element.isRequired) {
            console.log("collection 4")
            if (req.header(element.name) == null) {

              console.log("collection 5")
              validationFail = true
              return res.status(element.failMock.httpCode).send(element.failMock.responseMock)

            }
          }
        })
      }
      if (collection._body.length > 0) {
        console.log("collection 6")
        collection._body.forEach((element) => {
          console.log("collection 7")
          if (element.isRequired) {
            console.log("collection 8")
            if (req.body[element.name] === null || req.body[element.name] === undefined) {
              validationFail = true
              console.log(element)
              return res.status(element.failMock.httpCode).send(element.failMock.responseMock)
            }
          }

          if (element.condition !== null) {
            console.log("collection 9")
            element.condition.forEach((element) => {
              console.log("collection 10")
              if (req.body[element.when] === element.filledBy) {
                console.log("collection 11")
                validationFail = true
                return res.status(element.throw.httpCode).send(element.throw.responseMock)
              }
            })
          }
        })
      }
      if (!validationFail){
        console.log("close")
        return res.send(collection._payloadExmaple)
      }
    }).catch(excExtract => {
      next(excExtract)
    })
  } else {
    res.body('ok')
  }
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

module.exports = router
