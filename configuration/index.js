const dotEnv = require('dotenv')
const mongoose = require('mongoose')

module.exports.loadConfiguration = function (envPath) {
  dotEnv.config({
    path: envPath
  })
  this.environment = {
    host: process.env.HOST,
    port: process.env.PORT,
    database: {
      host: process.env.MONGO_HOST,
    },
    redis: {
      host: process.env.REDIS_HOST
    }
  }
  return this
}

module.exports.loadDatabase = function (config) {
  mongoose.connect(config.environment.database.host, {
    useNewUrlParser: true,
    poolSize: 4,
    useFindAndModify: false
  })
    .then(db => {
      console.log('mongo connected')
    }).catch(err => console.log('Mongo connection error', err))
  return mongoose
}
