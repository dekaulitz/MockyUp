let response = {
  type: {
    VALIDATION: {
      statusCode: 1001,
      httpCode: 400
    },
    DUPLICATE: {
      statusCode: 1002,
      httpCode: 400
    },
    INTERNAL_SERVER_ERROR: {
      statusCode: 2001,
      httpCode: 500
    },
    DATA_NOT_FOUND: {
      statusCode: 2002,
      httpCode: 400
    },
    SUCCESS: {
      statusCode: 3001,
      httpCode: 200
    }
  }
}

module.exports = response

module.exports.responseOK = function (statusConfig, data, message) {
  return this.status(statusConfig.httpCode).send({
    meta: {
      httpCode: statusConfig.httpCode,
      statusCode: statusConfig.statusCode,
      message: message
    },
    data: data
  })
}
module.exports.responseFail = function (statusConfig, message) {
  return this.status(statusConfig.httpCode).send({
    meta: {
      httpCode: statusConfig.httpCode,
      statusCode: statusConfig.statusCode,
      message: message
    },
    data: {}
  })
}
