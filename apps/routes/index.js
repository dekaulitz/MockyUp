let responseCode = require('./../helper/response')

const validation = require('../error_handler/validation.handler')
const express = require('express')
var cookieParser = require('cookie-parser')
var app = express()
/*
 end load configuration
 */
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(express.json())

app.get('/', function (req, res, next) {
  res.send('respond with a resource')
})
app.use('/v2', require('../controllers/mockupV2.controller'))
// catch 404 and forward to error handler
app.use(function (err, req, res, next) {
  console.log('error invoked 2' + err)
  if (err instanceof validation) {
    next(err.message)
  } else if (err instanceof TypeError) {
    return res.responseFail(responseCode.type.INTERNAL_SERVER_ERROR, err.message+" some property its not properly defined")
  }
  next(err)
})

module.exports = app
