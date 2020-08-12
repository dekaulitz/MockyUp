[![dekaulitz](https://circleci.com/gh/dekaulitz/MockyUp.svg?style=shield)](https://app.circleci.com/pipelines/github/dekaulitz/MockyUp)

# Simplify The Integration as Fast as You can

## MockyUp
MockyUp is an Openapi mock server that allow You to centralize the Openapi collections that can consuming You're Openapi spec and generating the mock as You want by You're own definitions. 
We are using `OpenApi Spesification version 3.x.x` and bundled with <a href="https://swagger.io/tools/swagger-ui/"> Swagger UI </a><br/>
For now we only supporting json request and xml request.

## Stacks
MockyUp build with SpringBoot and Swagger library and backed with mongodb as database and UI with vue js. For UI referencing you can check <a href="https://github.com/dekaulitz/mockup-frontend">Mockup UI Repository</a>.
We are creating new extension parameter on OpenApi spec for generating the mock response in this case we adding `x-examples` properties for set the mock.

## Requirements

* MongoDb 4.x.x.
* Java at least 1.8.x.
* Docker (optional).


## Features
Yes You can of course You can :

* You can mock the response base on Path parameter.
* You can mock the response base on Body request parameter.
* You can mock the response base on Header request parameter.
* You can mock the response base on Query string parameter.
* You can defining the HttpCode response.
* You can defining the Header properties.

In this case You have full control for the response as You want with You're own definitions base on criterias that You want to mock, and You can create mock user story base on the user properties or request properties that You want to try.
This will helping You to try perfect integration before the real integration before You're application ready.

## How to use
You can refer the example of OpenApi spec that already including the `x-examples` properties from <a href="https://raw.githubusercontent.com/dekaulitz/MockyUp/master/src/main/resources/public/example_mocking_books.json">here</a> 
or for more context you can check the documentation about OpenApi <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md">here</a>.<br/>

We adding new extension for set mock configuration. They are:

* x-query-including, it will matching the query string request.
* x-header-including, it will matching the header request.
* x-path-including, it will matching the path parameter request
* x-default, if you defined the x-default if above criterias does'nt matched it will rendering the response by default response.

### x-query-including,x-header-including,x-body-including and x-path-including configurations
```
         [{
              "property": {
                "name": "client-id",// this is the field property 
                "value": "empty" // this is the field value 
              },
              "response": {
                "httpCode": 200, //http code
                // you can add mock header response
                "headers": {
                  "x-request-id": "this is from mock", //header response added by mock
                },
                // if you want to mock the response body base on media type
                "content": {
                  "application/json": {
                    "response":{
                      // you can mock the response directly
                      "value": "something" 
                    }
                  },
                  "application/xml": {
                    // you can mock the response by the reference
                    "$ref": "#/components/examples/LIST_OF_BOOKS_EMPTY_XML"
                  }
                },
                // if you want add the response body direclty without media type
                "response":{
                  "value": "something"
               }
                // if you want to add the response body by the reference without media type
                "$ref": "#/components/examples/LIST_OF_BOOKS_EMPTY"
              }
            }]
```
You should defined the configuration if you want to mocking the response base on path parameter,query parameter, header parameter and body request like above.
if the property matched with request properties will rendering the response as the response request. 

### x-default configuration
```
         {
              "response": {
                "httpCode": 200,//http code
                // you can add mock header response
                "headers": {
                  "x-request-id": "this is from mock",//header response added by mock
                },
                 // if you want to mock the response body base on media type
                "content": {
                  "application/json": {
                    "$ref": "#/components/examples/LIST_OF_BOOKS_EMPTY"
                  },
                  "application/xml": {
                     // you can mock the response by the reference
                    "$ref": "#/components/examples/LIST_OF_BOOKS_EMPTY_XML"
                  }
                },
                // if you want add the response body direclty without media type
                "response":"#/components/examples/LIST_OF_BOOKS_EMPTY"
                 // if you want to add the response body by the reference without media type
                "$ref": "#/components/examples/LIST_OF_BOOKS_EMPTY"
              }
            }
```
This `x-default` configuration when the request is not matched with other configuration `x-default` will rendering the responsse.

### How to integrate
MockyUp will running as mock server and mock collection server. You can directly hit the MockypUp mocking endpoint for test mocking the response. 
```
http://[mockyup_hostname]/mocks/mocking/[mock_id]?path=[contract_endpoint]
```
Or do test via swagger 
```
http://[mockyup_hostname]/swagger/[mock_id]
```
Before that you should add new server env on youre spec.
```
    ...
    "servers":[
        {
              "url": "http://{host}/mocks/mocking/{mock_id}?path=",
              "description": "Testing locally",
              "variables": {
                "host": {
                  "default": "localhost:7070"
                },
                "mock_id": {
                  "description": "mock id from database"
                }
              }
            }
    ...
     ]
```
You can check the example configuration spec from <a href="https://raw.githubusercontent.com/dekaulitz/MockyUp/master/src/main/resources/public/example_mocking_books.json">here</a> 
## Supported
<a href=" https://www.jetbrains.com/?from=MockyUp"> ![Image of jetbrain](https://www.jetbrains.com/company/press/#images-logos)</a>




