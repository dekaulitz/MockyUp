var createError = require('http-errors')

const validation = require('../error_handler/validationHandler')
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
  if (err instanceof validation) {

  }
  next(err)
})
module.exports = app
