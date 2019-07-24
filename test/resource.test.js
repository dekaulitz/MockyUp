module.exports.dummy = {
  '_name': 'Sample mockup',
  '_desc': 'this is the sample of mockup structure',
  '_path': '/v1/api/register',
  '_method': 'POST',
  '_header': [
    {
      'name': 'signature',
      'isRequired': true,
      'throw': {
        'httpCode': 400,
        'result': {
          'statusCode': 5001,
          'message': 'hmac is required'
        }
      },
      'conditions': [
        {
          'when': {
            'filledBy': 'test_hmac',
            'httpCode': 400,
            'result': {
              'statusCode': 4001,
              'message': 'invalid hmac'
            }
          }
        }
      ]
    },
    {
      'name': 'app-id',
      'isRequired': true,
      'throw': {
        'httpCode': 400,
        'result': {
          'statusCode': 5001,
          'message': 'app-id is required'
        }
      },
      'conditions': [
        {
          'when': {
            'filledBy': 'v.1.23',
            'httpCode': 400,
            'result': {
              'statusCode': 4002,
              'message': 'invalid version'
            }
          }
        }
      ]
    }
  ],
  '_body': {
    'type': 'object',
    'consumes': 'application/json',
    'values': [
      {
        'name': 'firstName',
        'type': 'string',
        'isRequired': true,
        'throw': {
          'httpCode': 400,
          'result': {
            'statusCode': 40012,
            'message': 'firstname is required'
          }
        },
        'conditions': [
          {
            'when': {
              'filledBy': 'fahmi',
              'httpCode': 400,
              'result': {
                'statusCode': 4002,
                'message': 'user already exist'
              }
            }
          },
          {
            'when': {
              'filledBy': 'ajo',
              'httpCode': 400,
              'result': {
                'statusCode': 4002,
                'message': 'user already block',
                'meta': {
                  'debugId': '123-123-123'
                }
              }
            }
          }
        ]
      },
      {
        'name': 'email',
        'type': 'string',
        'isRequired': true,
        'throw': {
          'httpCode': 400,
          'result': {
            'statusCode': 5003,
            'message': 'email is required'
          }
        },
        'conditions': [
          {
            'when': {
              'filledBy': 'fahmi.sulaiman@ovo.id',
              'httpCode': 422,
              'result': {
                'statusCode': 4002,
                'message': 'email already registered'
              }
            }
          },
          {
            'when': {
              'filledBy': 'ajo@gmail.com',
              'httpCode': 500,
              'result': {
                'statusCode': 4002,
                'message': 'email not valid'
              }
            }
          }
        ]
      },
      {
        'name': 'address',
        'type': 'string',
        'isRequired': false,
        'throw': null,
        'conditions': [
          {
            'when': {
              'filledBy': 'Menara kuningan',
              'httpCode': 400,
              'result': {
                'statusCode': 4001,
                'message': 'location already blocked'
              }
            }
          }
        ]
      }
    ],
    'isRequired': true,
    'throw': {
      'httpCode': 400,
      'result': {
        'statusCode': 5000,
        'message': 'internal server error',
        'meta': {
          'timestamp': 123123123123,
          'status': false
        }
      }
    }
  },
  '_defaultResponse': {
    'throw': {
      'httpCode': 200,
      'result': {
        'name': 'fahmi sulaiman',
        'email': 'fahmi.sulaiman@ovo.id',
        'address': 'SCBD'
      }
    }
  }
}

module.exports.abnormalHeaderCase1 = {
  '_header': [
    {
      'name2': 'signature',
      'isRequired': true,
      'throw': {
        'httpCode': 400,
        'results': {
          'statusCode': 5001,
          'message': 'hmac is required'
        }
      },
      'conditions': [
        {
          'when': {
            'filledBy': 'test_hmac',
            'httpCode': 400,
            'result': {
              'statusCode': 4001,
              'message': 'invalid hmac'
            }
          }
        }
      ]
    }
  ]

}



