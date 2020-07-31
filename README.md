[![dekaulitz](https://circleci.com/gh/dekaulitz/MockyUp.svg?style=shield)](https://app.circleci.com/pipelines/github/dekaulitz/MockyUp)

<h2>Simplify The Integration as Fast as You can</h2>
<h2>MockyUp</h2> 
MockyUp is an Openapi mock server that allow You to centralize the Openapi collections that can consuming You're Openapi spec and generating the mock as You want by You're own definitions. 
We are using `OpenApi Spesification version 3.x.x` and bundled with <a href="https://swagger.io/tools/swagger-ui/"> Swagger UI </a><br/>
For now we only supporting json request and json response or string response.


<h2>Stacks</h2>
MockyUp build from SpringBoot and Swagger library and backed with mongodb as database and UI with vue js. For UI referencing you can check <a href="https://github.com/dekaulitz/mockup-frontend">Mockup UI Repository</a>.
We are creating new extension parameter on OpenApi spec for generating the mock response in this case we adding `x-examples` properties for set the mock.

<h2>Requirements</h2>
* MongoDb 4.x.x
* Java at least 1.8.x
* Docker (optional)


<h2>Features</h2>
Yes You can of crouse You can :
* You can mock the response base on Path parameter.
* You can mock the response base on Body request parameter.
* You can mock the response base on Header request parameter.
* You can mock the response base on Query string parameter.
* You can defining the HttpCode response.
* You can defining the Header properites.

In this case You has full control for the response as You want with You're own definition base on criterias that You want to mock, and You can create mock user story base on the user properties or request properties that You want to try.
This will helping You to try perfect integration before the real integration before You're application ready.

<h2>How to use</h2>
You can refer the example of OpenApi spec that already including the `x-examples` properties from <a href="https://raw.githubusercontent.com/dekaulitz/MockyUp/master/src/main/resources/public/example_mocking_books.json">here</a>.<br/>
Change the application properties that matched with You're machine.
For `x-examples` extension that has some attributes. They are :

* x-query-including, it will matching the query string request.
* x-header-including, it will matching the header request.
* x-path-including, it will matching the path parameter request
* x-default, if you defined the x-default if above criterias does'nt matched it will rendering the response by default response.

And then you run the Appilication like usuall or using `docker-compose`.
For the first time Application will check the user root is exist or not, if the user was not exist Application will create the user root with password root by default.

Do login with the user that already created. And create new spec or just paste example spec from <a href="https://raw.githubusercontent.com/dekaulitz/MockyUp/master/src/main/resources/public/example_mocking_books.json">here</a>
and open the spec and choose swagger ui and edit `testing_path` with id database and `host` to You're application host like `localhost:7070`.
And start to request.







