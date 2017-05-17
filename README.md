

## ifc to idf  REST service

An ifc to idf REST Service built using *Spring Boot* framework and *Maven*.

- use *Git* clone to make a clone of the repository.
- Open a terminal
- change directory to have the same directory which contains `pom.xml` file

		$ mvn spring-boot:run
- if you want to run a jar package run first:

		$ mvn clean package
- once the jar package is created run:

- api routes:

##### Products(Elements):

-`/api/sendfile` - this is `POST` route. required parameters: key: `ifcFile` and the real if file as `Multipart` input. This will send the file to deserializer service reading the unique categories/products;

`/api/products` - this is `GET` route. basically, once the post is done, this controller will read the output providing a `JSON` list.

## Tests

Several unit tests are included in order to test the code in an agile way. The test can be run in your IDE or in your terminal running the following command:

	 $ mvn clean package

## Contributors

	TODO: Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

	TODO: A short snippet describing the license (MIT, Apache, etc.)
