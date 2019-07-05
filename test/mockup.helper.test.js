let mockupHelper = require('../apps/helper/mockup.helper')
const assert = require('chai').assert

describe('mockup helper tranform header', () => {
  let properties = [
    {
      'name': 'hmac',
      'type': 'string',
      'isRequired': true,
      'throw': {
        'httpCode': 400,
        'result': {
          'statusCode': 500,
          'message': 'hmac is required'
        }
      },
      'conditions': [
        {
          'when': {
            'filledBy': '123',
            'httpCode': 400,
            'result': {
              'statusCode': 500,
              'message': 'hmac is something'
            }
          }
        },
        {
          'when': {
            'filledBy': '1',
            'httpCode': 422,
            'result': {
              'statusCode': 500,
              'message': 'something shit happen'
            }
          }
        }
      ]
    },
    {
      'name': 'appId',
      'type': 'string',
      'isRequired': true,
      'throw': {
        'httpCode': 400,
        'result': {
          'statusCode': 500,
          'message': 'appId is required'
        }
      }
    },
    {
      'name': 'time',
      'isRequired': true,
      'throw': {
        'httpCode': 422,
        'result': {
          'statusCode': 500,
          'message': 'time is required'
        },
        'conditions': [
          {
            'when': {
              'filledBy': '123',
              'httpCode': 422,
              'result': {
                'statusCode': 500,
                'message': 'hmac is required'
              }
            }
          },
          {
            'when': {
              'filledBy': '1',
              'httpCode': 400,
              'result': {
                'statusCode': 500,
                'message': 'something shit happen'
              }
            }
          }
        ]
      }
    }
  ]
  it('transform the header and result success', async function () {

    let requestHeader = {
      'hmac': '123123123',
      'appId': '123123123',
      'time': '123123123'
    }
    let transformHeader = await mockupHelper.transformHeader(properties, requestHeader)
    assert.equal(transformHeader, null)
  })
  it('transform the header with some hmac is missing', async function () {
    let requestHeader = {
      'appId': '123123123',
      'time': '123123123'
    }
    let transformHeader = await mockupHelper.transformHeader(properties, requestHeader)
    assert.equal(transformHeader.httpCode, 400)
  })
  it('transfrom the header with appId is missing', async function () {
    let requestHeader = {
      'hmac': '123123123',
      'time': '123123123'
    }
    let transformHeader = await mockupHelper.transformHeader(properties, requestHeader)
    assert.equal(transformHeader.httpCode, 400)
  })
  it('transform the header with some value has condition and return http code 422', async function () {
    let requestHeader = {
      'hmac': '1',
      'appId': '123123',
      'time': '1'
    }
    let transfrormHeader = await mockupHelper.transformHeader(properties, requestHeader)
    assert.equal(transfrormHeader.httpCode, 422)
  })
  it('transform the header with some value has condition and return http code 400', async function () {
    let requestHeader = {
      'hmac': '123',
      'appId': '123123',
      'time': '123'
    }
    let transfrormHeader = await mockupHelper.transformHeader(properties, requestHeader)
    console.log(transfrormHeader)
    assert.equal(transfrormHeader.httpCode, 400)
  })
})

describe('mockup helper transform body', () => {
  describe('mockup helper transfrom body with array object', () => {
    let properties = {
      'type': 'object',
      'consumes': 'application/json',
      'values': [
        {
          'name': 'id',
          'type': 'string',
          'isRequired': true,
          'throw': {
            'httpCode': 400,
            'result': {
              'statusCode': 422,
              'message': 'id is required'
            }
          },
          'conditions': [
            {
              'when': {
                'filledBy': '12asd',
                'httpCode': 4012,
                'result': {
                  'statusCode': 422,
                  'message': 'id doesnt exist'
                }
              }
            }
          ]
        },
        {
          'name': 'name',
          'type': 'string',
          'isRequired': false,
          'conditions': [
            {
              'when': {
                'filledBy': 'fahmi',
                'httpCode': 422,
                'result': {
                  'statusCode': 422,
                  'message': 'name is already exist'
                }
              }
            },
            {
              'when': {
                'filledBy': 'ajo',
                'httpCode': 500,
                'result': {
                  'statusCode': 500,
                  'message': 'internal server error'
                }
              }
            }
          ]
        },
        {
          'name': 'brothers',
          'type': 'arrayObject',
          'isRequired': true,
          'throw': {
            'httpCode': 400,
            'result': {
              'statusCode': 422,
              'message': 'id is required'
            }
          },
          'conditions': null
        }
      ]
    }

    it('transform body with everythin is good and result success', async () => {
      let requestBody = {
        'id': '123123',
        'name': 'asdasd',
        'brothers': [
          { 'name': 'asd' },
          { 'name': 'ajo' }
        ]
      }
      let transfromBody = await mockupHelper.transformBody(properties, requestBody)
      assert.equal(transfromBody, null)
    })
    it('transform body with some body is missing', async () => {
      let requestBody = {
        'name': 'fahmi',
        'brothers': [
          { 'name': 'asd' },
          { 'name': 'ajo' }
        ]
      }
      let transfromBody = await mockupHelper.transformBody(properties, requestBody)
      assert.equal(transfromBody.httpCode, 400)
    })
    it('transform body with some body is throw an error', async () => {
      let requestBody = {
        'id':'1',
        'name': 'ajo',
        'brothers': [
          { 'name': 'asd' },
          { 'name': 'ajo' }
        ]
      }
      let transfromBody = await mockupHelper.transformBody(properties, requestBody)
      assert.equal(transfromBody.httpCode, 500)
    })
  })
  describe('mockup helper transfrom body with arrayObject',async () => {
    let properties = {
      'type': 'arrayObject',
      'consumes': 'application/json',
      'values': [
        {
          'name': 'id',
          'type': 'string',
          'isRequired': true,
          'throw': {
            'httpCode': 400,
            'result': {
              'statusCode': 422,
              'message': 'id is required'
            }
          },
          'conditions': [
            {
              'when': {
                'filledBy': '12asd',
                'httpCode': 4012,
                'result': {
                  'statusCode': 422,
                  'message': 'id doesnt exist'
                }
              }
            }
          ]
        },
        {
          'name': 'name',
          'type': 'string',
          'isRequired': false,
          'conditions': [
            {
              'when': {
                'filledBy': 'fahmi',
                'httpCode': 422,
                'result': {
                  'statusCode': 422,
                  'message': 'name is already exist'
                }
              }
            },
            {
              'when': {
                'filledBy': 'ajo',
                'httpCode': 500,
                'result': {
                  'statusCode': 500,
                  'message': 'internal server error'
                }
              }
            }
          ]
        },
        {
          'name': 'brothers',
          'type': 'arrayObject',
          'isRequired': true,
          'throw': {
            'httpCode': 400,
            'result': {
              'statusCode': 422,
              'message': 'id is required'
            }
          },
          'conditions': null
        }
      ]
    }
    it('transfrom body with some object is throw an error',async ()=>{
      let requestBody = [
        {
          'id':'12asd',
          'name': 'ajo',
          'brothers': [
            { 'name': 'asd' },
            { 'name': 'ajo' }
          ]
        },
        {
          'id':'2',
          'name': 'fahmi',
          'brothers': [
            { 'name': 'asd' },
            { 'name': 'ajo' }
          ]
        }
      ]
      let transfromBody = await mockupHelper.transformBody(properties, requestBody)
      assert.equal(transfromBody.httpCode, 4012)
    })

  })

})

