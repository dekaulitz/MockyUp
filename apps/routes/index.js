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
  next(createError(err))
})
app.get('/', function (req, res, next) {
  res.send('respond with a resource')
})
app.use('/v1/mockup', require('../controllers/mockup.controller'))
// catch 404 and forward to error handler

app.use(function (err, req, res, next) {
  res.status(500)
  res.status(500).send(err.message)
})
module.exports = app
