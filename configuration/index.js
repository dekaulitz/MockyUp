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
      username: process.env.MONGO_USERNAME,
      password: process.env.MONGO_PASSWORD,
      host: process.env.MONGO_HOST,
      schema: process.env.MONGO_DATABASE
    },
    redis: {
      host: process.env.REDIS_HOST
    }
  }
  return this
}

module.exports.loadDatabase = function (config) {
  mongoose.connect('mongodb://' + config.environment.database.host + '/' + config.environment.database.schema,
    {
      useNewUrlParser: true,
      poolSize: 4
    })
    .then(db => {
      console.log('mongo connected')
    }).catch(err => console.log('Mongo connection error', err))
  return mongoose
}
