[![dekaulitz](https://circleci.com/gh/dekaulitz/MockyUp.svg?style=shield)](https://app.circleci.com/pipelines/github/dekaulitz/MockyUp)

<h2 align="left">MOCKYUP</h2>
MockyUp is a mockup server that release with java version that following OASOPENAPI version 3.xx. You can easyly to integration your swagger documentation with this mockup server with injecting some vendor extension for generating some example.
**We only supported for json request and response**.

For  <a href="https://github.com/dekaulitz/mockup-frontend">Mockup UI Repository </a>
<h2 align="left">Stack</h2>
This project build under spring boot framework with spring data mongodb and <a href="https://github.com/swagger-api">swagger library version 3 </a>.

<h2 align="left"><b>Documentation</b></h2>
<h4>Requirement</h4>
Pre requirement you must have:

* MongoDB
* Java absolutely at least 1.8 version
* Postman
* Open API Spec version 3.0.x its mandatory if you want to mock response works


<h4>How to run it</h4>
Changes your application.properties if you have different configuration of mongodb installment.

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
For swagger ui  we are using custom swagger ui that for enable mockup path
```
http://localhost:8080/docs-swagger/index.html
```

when the apps running will check the default account `root` if account root its not exist will create the new one with password `root` * youre should change latter if you are already using the previous mock, will also adding users into every mock that has not users.


<h4>How to Add Example on Swagger OAS 3</h4>
On Openapi version 3 (OAS3) there is some feature that called vendor extension.
so im just adding new extension fields:

* `x-examples` new extension for add examples
* `x-header-including` new extenstion sub from `x-examples` related with the headers example
* `x-path-including` new extension sub from `x-examples` related with paths example
* `x-body-including` new extension sub from `x-examples` related with body example
* `x-query-including` new extension sub from `x-examples` related with query example
* `x-default` new extension sub from `x-examples` related with default example

<h4>Swagger with mocks</h4>
You can see how to integrate swagger.json with the mock response with this <a href="https://github.com/dekaulitz/MockyUp/blob/master/src/main/resources/public/example_mocking_books.json">structure</a>.
change the host with the current host and change `testing_path` with the id from database and check your request.
