<h2 align="left">MOCKYUP</h2>
MockyUp is a mockup server that release with java version that following OASOPENAPI version 3.xx. You can easyly to integration your swagger documentation with this mockup server with injecting some vendor extension for generating some example.

<h2 align="left">Stack</h2>
This project build under spring boot framework with spring data mongodb and <a href="https://github.com/swagger-api">swagger library version 3 </a>.

<h2 align="left"><b>Documentation</b></h2>
<h4>Requirement</h4>
Pre requirement you must have:

* Monngodb 
* Java absolutely at least 1.8 version
* Postman


<h4>How to run it</h4>
Changes your applicatio.properties if you have different configuration of mongodb installment.

For build the jar and downloading the library
```
mvn install
```
for running the jar 
```
java -jar target/mockyup-0.0.1-SNAPSHOT.jar
```
for check swagger docs url
```
http://localhost:8080
```

<h4>How to Add Example on Swagger OAS 3</h4>
On Openapi version 3 (OAS3) there is some feature that called vendor extension.
so im just adding new extension fields:

* `x-examples` new extension for add examples
* `x-header-including` new extenstion sub from `x-examples` related with the headers example
* `x-path-including` new extension sub from `x-examples` related with paths example
* `x-body-including` new extension sub from `x-examples` related with body example
* `x-query-including` new extension sub from `x-examples` related with query example

<h4>Example structures</h4> 

```
..some swagger structure
   "paths": {
            "/api/v1.0/registration/{type}": {
                "post": {
                    "tags": [
                        "registration"
                    ],
                    "summary": "Process Registration",
                    "operationId": "registrationUser",
                    "parameters": [
                              {
                                "in": "path",
                                "name": "type",
                                "required": true,
                                "description": "id from mongodb collection",
                                "schema": {
                                  "type": "string"
                                }
                              },
                            {
                                "in": "header",
                                "name": "app-id",
                                "required": true,
                                "schema": {
                                  "type": "string"
                                }
                            },
                             {
                                "in": "query",
                                "name": "from",
                                "required": false,
                                "schema": {
                                  "type": "string"
                                }
                            },
                    ],
                    "requestBody": {
                        "content": {
                            "application/json": {
                                "schema": {
                                    "type": "object",
                                    "properties": {
                                      "firstName": {
                                        "type": "string"
                                      },
                                      "lastName": {
                                        "type": "string"
                                      },
                                       "email":{
                                        "type":"string"
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "x-examples": {
                        "x-header-including": [
                            {
                                "property": {
                                	"name":"app-id",
                                	"value":"123321"
                                },
                                "response": {
                                    "httpCode": 500,
                                    "response": {
                                        "statusCode": 500,
                                        "message": "internal server error",
                                        "timestamp":12321123123
                                    }
                                }
                            }
                        ],
                        "x-path-including": [
                        	{
                                "property": {
                                	"name":"type",
                                	"value":"admin"
                                },
                                "response": {
                                    "httpCode": 200,
                                    "response": {
                                        "statusCode": 200,
                                        "data":{
                                           "name":"admin1",
                                           "email":"test from admin type"
                                       }                    
                                    }
                                }
                            },
                            {
                                "property": {
                                	"name":"type",
                                	"value":"docs"
                                },
                                "response": {
                                    "httpCode": 200,
                                    "response": {
                                        "statusCode": 4300,
                                        "message": "dosc success"
                                    }
                                }
                            }
                        	],
                        "x-query-including": [
                        	 {
                                "property": {
                                	"name":"from",
                                	"value":"indonesia"
                                },
                                "response": {
                                    "httpCode": 200,
                                    "response": {
                                        "statusCode": 4300,
                                        "message": "qName success",
                                        "httpCode": 200
                                    }
                                }
                            },
                             {
                                "property": {
                                	"name":"total",
                                	"value":"1"
                                },
                                "response": {
                                    "httpCode": 200,
                                    "response": {
                                        "statusCode": 4300,
                                        "message": "qValue success"
                                    }
                                }
                            }
                        	],
                        "x-body-including": [
                        	{
                                "property": {
                                	"name":"firstName",
                                	"value":"fahmi"
                                },
                                "response": {
                                    "httpCode": 200,
                                    "response": {
                                        "statusCode": 4300,
                                        "message": "success"
                                    }
                                }
                            },
                                {
                                "property": {
                                	"firstName":"name",
                                	"value":"test"
                                },
                                "response": {
                                    "httpCode": 404,
                                    "response": {
                                        "statusCode": 4004,
                                        "message": "data not found"
                                    }
                                }
                            }
                        	],
                        "x-default": {
                          "property": {},
                          "response": {
                            "httpCode": 404,
                            "response": {
                              "statusCode": 4004,
                              "message": "data not found",
                              "data": {
                                "default": "x-response default"
                              }
                            }
                          }
                        }
                    },
                    "responses": {
                        ..your swagger response
                    }
                }
            }
        },
..some swagger 
```
For generating the mocks, mockyup generate by order which is if the some header already match with the mocks criteria its will throw the response. ordering the response its deplending how you put `x-examples` value.

example for hit example above:
- headers`curl --location --request POST 'localhost:8080/mocks/mocking/{_id_from_mongo}?path=/api/v1.0/registration/ds' \
           --header 'app-id: 123321' \
           --header 'Content-Type: application/json' \
           --data-raw '{
           	"firstName":"test"
           }'`
- path `curl --location --request POST 'localhost:8080/mocks/mocking/5e282fe8034446470a268362?path=/api/v1.0/registration/admin' \
        --header 'app-id: 123321s' \
        --header 'signature: 123321s' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        	"firstName":"test"
        }'`
- query `curl --location --request POST 'localhost:8080/mocks/mocking/5e282fe8034446470a268362?path=/api/v1.0/registration/admin2?from=indonesia' \
         --header 'app-id: 123321s' \
         --header 'signature: 123321s' \
         --header 'Content-Type: application/json' \
         --data-raw '{
         	"firstName":"test"
         }'`
- body `curl --location --request POST 'localhost:8080/mocks/mocking/5e282fe8034446470a268362?path=/api/v1.0/registration/admin2?from=indonesia1' \
        --header 'app-id: 123321s' \
        --header 'signature: 123321s' \
        --header 'Content-Type: application/json' \
        --data-raw '{
        	"firstName":"fahmi"
        }'`
- default `curl --location --request POST 'localhost:8080/mocks/mocking/5e29322f98a1c001699467a1?path=/api/v1.0/registration/admin2?from=indonesia1' \
           --header 'app-id: 123321s' \
           --header 'signature: 123321s' \
           --header 'Content-Type: application/json' \
           --data-raw '{
           	"firstName":"faahmi"
           }'`
