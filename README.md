# REST service project

This project consists of three modules: a parent module, `ms-personnel-modules`, which contains shared dependencies for the entire project, and thre child modules, `person-service`, `person-client` and `person-model`.

## person model

The person-model contains information about DTO classes for managing data between the service and client layers.

## person client

The person-client module sends HTTP requests using GET, POST, PUT, and DELETE methods to perform basic CRUD operations. It utilizes Jackson to serialize objects into JSON format and deserialize them into POJOs using ObjectMapper. Additionally, a custom utility class, CustomPageImpl, has been introduced to facilitate the deserialization of responses from APIs that implement pagination.

## person-service

### PersonRestController

The `PersonRestController` class handles REST endpoints related to `Person` entities. It provides endpoints for creating, updating, deleting, and querying `Person` objects. Additionally, pagination support has been implemented to enhance the efficiency of data retrieval.

The controller uses validation annotations for input data and handles exceptions by returning appropriate error responses. It also converts between `Person` entities and `PersonDTO` objects using the `PersonDTOMapper` class.

The controller includes exception handling for PersonNotFoundException. This exception handling is managed by a ControllerAdvice.

Integration tests have been added using the Testcontainers PostgreSQL container. 

To optimize performance under high load, a queue system has been implemented. In a separate thread, QueueListener retrieves data and saves it to the repository. Batched queries are used to optimize database interactions.
The database is deployed in a Docker container for easy deployment and testin
