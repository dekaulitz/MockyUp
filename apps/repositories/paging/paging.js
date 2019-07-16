const helper = require('./helper')

const pagination = function () {
  helper.call(this)
}
pagination.prototype = Object.create(helper.prototype)

pagination.prototype.getAllProperty = function (req, callback) {
  this.extractRequest(req, (property) => {
    this.model.find(property.query).count().exec().then((count) => {
      this.model.find(property.query).select(this.select).sort(this.sorting).exec().then((data) => {
        callback(data)
      }).catch((err) => console.log(err))
    }).catch((err) => console.log(err))
  })
}

pagination.prototype.getPagination = async function (req, callback) {
  let property = await this.extractRequest(req)
  this.model.find(property.query).countDocuments().exec().then((count) => {
    let skip = parseInt(property.size) * (parseInt(property.page) - 1)
    this.model.find(property.query).limit(parseInt(property.size)).select(this.select).skip(skip).sort(this.sorting).exec().then((data) => {
      let paging = {
        rows: data,
        rowCount: count,
        page: this.page,
        size: this.size,
        pageCount: Math.ceil(count / parseInt(this.size))
      }
      callback(null, paging)
    }).catch((err) => callback(err, null))
  }).catch((err) => callback(err, null))
}
module.exports = pagination
