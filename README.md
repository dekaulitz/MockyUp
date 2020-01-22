<h2 align="left">MOCKY Up</h2>
MockyUp is a mockup server that release with java version that following OASOPENAPI version 3.xx. You can easyly to integration your swagger documentation with this mockup server with injecting some vendor extension for generating some example.

<h2 align="left">Stack</h2>
This project build under spring boot framework with spring data mongodb and <a href="https://github.com/swagger-api">swagger library version 3 </a>.

<h2 align="left"><b>Documentation</b></h2>
<h4>Requirement</h4>
Pre requirement you must have
 - Monngodb 
 - Java absolutely at least 1.8 version
 - Postman

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

<h4>How to Add Example on Swagger OAS 3</h4>
On Openapi version 3 (OAS3) there is some feature that called vendor extension

