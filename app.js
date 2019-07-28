var express = require('express')
var app = express()

app.response.responseFail = require('./apps/helper/response').responseFail
app.response.responseOk = require('./apps/helper/response').responseOK
/*
Load configuration
 */
let envPath = process.env.NODE_ENV === undefined ? '.env.test' : '.env.' + process.env.NODE_ENV
let config = require('./configuration')
config.loadConfiguration(envPath)

const db = config.loadDatabase(config).connection
db.on('error', console.error.bind(console, 'connection error:'))
db.once('open', function () {
  console.log('db connected')
})

app.use('/docs',express.static('public'))
app.use(require('./apps/routes'))

app.use(function (req, res, next) {
  return res.status(404).send("Sorry can't find that!")
})
const server = app.listen(config.environment.port, () => {
  console.log('Listen on port ' + config.environment.port)
})
const sigs = ['SIGINT', 'SIGTERM', 'SIGQUIT']
sigs.forEach(sig => {
  process.on(sig, () => {
    console.log(sig)
    // Stops the server from accepting new connections and finishes existing connections.
    server.close(function (err) {
      if (err) {
        console.error(err)
        process.exit(1)
      }
      // close your database connection and exit with success
      // for example with mongoose
      db.close(function () {
        console.log('Mongoose connection disconnected')
        process.exit(0)
      })
    })
  })
})
