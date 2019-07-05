var createError = require('http-errors')
const express = require('express')
var cookieParser = require('cookie-parser')
var app = express()
/*
 end load configuration
 */
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(express.json())
app.use(function (err, req, res, next) {
  console.log("error invoked")
  next(createError(err))
})
app.get('/', function (req, res, next) {
  res.send('respond with a resource')
})
app.use('/v2', require('../controllers/mockupV2.controller'))
// catch 404 and forward to error handler

app.use(function (err, req, res, next) {
  console.log("error invoked")
  next(err)
})
module.exports = app
