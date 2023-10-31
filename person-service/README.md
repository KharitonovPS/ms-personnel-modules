# Multi-Module Maven Project

This project consists of three modules: a parent module, `ms-personnel-modules`, which contains shared dependencies for the entire project, and thre child modules, `person-service`, `person-client` and `person-model`.

## person-model

The person-model contains information about DTO classes for managing data between the service and client layers.

## http-client-module

The hperson-client module sends HTTP requests using GET, POST, PUT, and DELETE methods to perform basic CRUD operations. It utilizes Jackson to serialize objects into JSON format and deserialize them into POJOs using ObjectMapper. Additionally, a custom utility class, CustomPageImpl, has been introduced to facilitate the deserialization of responses from APIs that implement pagination.

## person-service

### LoadDatabase

The `LoadDatabase` class initializes the database with sample data. It uses Spring Boot's `CommandLineRunner` to run the data initialization logic when the application starts. Sample data includes `Person` objects with names and ages.

### PersonRestController

The `PersonRestController` class handles REST endpoints related to `Person` entities. It provides endpoints for creating, updating, deleting, and querying `Person` objects. Additionally, it offers endpoints for various operations on `Person` objects, such as filtering, sorting, and statistical analysis.

The controller uses validation annotations for input data and handles exceptions by returning appropriate error responses. It also converts between `Person` entities and `PersonDTO` objects using the `PersonDTOMapper` class.

The controller includes exception handling for PersonNotFoundException. This exception handling is managed by a ControllerAdvice.

Integration tests have been added using the Testcontainers PostgreSQL container. Requests are sent via HTTP to the API using the PersonClient. Additionally, unit tests have been written where, instead of the client, the WebApplicationContext and MockMvc are used.

