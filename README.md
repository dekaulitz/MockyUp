<p align="center">
  <a href="https://travis-ci.org/dekaulitz/mockup-server"><img src="https://travis-ci.org/dekaulitz/mockup-server.svg?branch=master" alt="Build Status"></a>
</p>

<h2 align="center">MockyUp</h2>
Mockyup its a mockup server build under node.js for generating mockup response that usually used by frontend enginer for build an application.

###Introduction
MockyUp build with <a href="https://nodejs.org" alt="node js">node.js</a> and <a href="https://expressjs.com/">express.js</a> framework with flexible noSql mongodb
that aiming for build mockup contract for integration service with easy and readable. With eazy structure that helps for generating contract as fast as possible for fast development.
Main features:
- Header validation with default response and custom response.
- Body validation with default response and custom response
###How to use
Edit .env.test with your environment and run for development

```
npm run dev-run
```
for production
```
npm run start
```
for testing
```
npm run test
```

<h2 align="center">Mockups Structure</h2>

| field | Description |
|---------|-------------|
|_name|Endpoint name|
|_desc|Endpoint description which is  the purpose endpoint description
|_method| Http method that use for access the endpoint
|_header| Request header, you can use signature or authorization that uses by endpoint
|_body| Body json that can pass to endpoint
|_defaultResponse| Default response that will be throw if the requrement has been filled


###Example
This is an example of structure of mockup contract
```
_name: prototype mockup server
_desc: describe prototype mockup
_path: /v1/api/users
_method: get
_header:
  - name: hmac
    type: string
    isRequired: true
    throw:
      httpCode: 400
      result:  {"statusCode":500,"message":"hmac is required"}
    conditions:
      - when:
          filledBy: 123
          httpCode: 400
          result: {"statusCode":500,"message":"hmac is required"}
      - when:
          filledBy: 1
          httpCode: 400
          result: {"statusCode":500,"message":"something shit happen"}
  - name: appId
    type: string
    isRequired: true
    throw:
      httpCode: 400
      result: {"statusCode":500,"message":"appId is required"}
  - name: time
    isRequired: true
    throw:
      httpCode: 400
      result: {"statusCode":500,"message":"time is required"}
_body:
  type: object
  consumes: application/json
  values:
    - name: id
      type: string
      isRequired: true
      throw:
        httpCode: 400
        result: {"statusCode":422,"message":"id is required"}
      conditions:
        - when:
            filledBy: 1
            httpCode: 422
            result: {"statusCode":422,"message":"id doesnt exist"}
    - name: name
      type: string
      isRequired: false
      conditions:
        - when:
            filledBy: fahmi
            httpCode: 422
            result: {"statusCode":422,"message":"name is already exist"}
        - when:
            filledBy: ajo
            httpCode: 500
            result: {"statusCode":500,"message":"internal server error"}
  isRequired: true
  throw:
    httpCode: 200
    result: {"statusCode":200,"message":"success"}
_defaultResponse:
  throw:
    httpCode: 200
    result: {"statusCode":200,"message":"success"}
```

Every property has values and conditions that can help you to match the requirement

```
_header:
  - name: hmac
    type: string
    isRequired: true
    throw:
      httpCode: 400
      result:  {"statusCode":500,"message":"hmac is required"}
    conditions:
      - when:
          filledBy: 123.123123-123123
          httpCode: 400
          result: {"statusCode":500,"message":"hmac is required"}
```
The structure above determaining the property and the condition if ``isRequired`` its empty its will be throw with ``httpCode 400``
and response ``{"statusCode":500,"message":"hmac is required"}`` but when has a condition and the ``is_required`` 
is fulfilled its steping to the conditions. if hmac filled by 123.123123-123123 its will be throw ``httpCode 400`` 
with default response ``{"statusCode":500,"message":"hmac is required"}``

And for the body

```
_body:
  type: object
  consumes: application/json
  values:
    - name: id
      type: string
      isRequired: true
      throw:
        httpCode: 400
        result: {"statusCode":422,"message":"id is required"}
      conditions:
        - when:
            filledBy: 1
            httpCode: 422
            result: {"statusCode":422,"message":"id doesnt exist"}
    - name: name
      type: string
      isRequired: false
      conditions:
        - when:
            filledBy: fahmi
            httpCode: 422
            result: {"statusCode":422,"message":"name is already exist"}
        - when:
            filledBy: ajo
            httpCode: 500
            result: {"statusCode":500,"message":"internal server error"}
  isRequired: true
  throw:
    httpCode: 400
    result: {"statusCode":323,"message":"body its required"}
_defaultResponse:
  throw:
    httpCode: 200
    result: {"statusCode":200,"message":"success"}
```

The structure above determaining the property and the condition of the body payload  if ``isRequired`` its empty 
its will be throw with ``httpCode 400`` and response ``{"statusCode":323,"message":"body its required"}`` 
but when has a condition and the ``is_required`` is fulfilled its steping to the conditions. 

