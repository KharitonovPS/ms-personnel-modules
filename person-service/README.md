# Multi-Module Maven Project

This project consists of three modules: a parent module, `multi-module`, which contains shared dependencies for the entire project, and two child modules, `http-client-module` and `ms-person-module`.

## http-client-module

The `http-client-module` module sends HTTP requests using GET, POST, PUT, and DELETE methods. It uses Jackson to serialize objects into JSON format and deserialize them into POJOs using ObjectMapper.

### GET Requests

- **findAllPerson:** Fetches a list of all persons from the server.
- **getPerson(id):** Fetches a specific person by ID.
- **filterMultiplyEven:** Fetches persons with even ages.
- **sortByAge:** Fetches persons sorted by age.
- **sortByFirstCharOfName:** Fetches persons sorted by the first character of their names.
- **findMaxAge:** Fetches the person with the maximum age.
- **skipAndLimitation:** Fetches a limited number of persons with optional skipping.
- **personNamesAsString:** Fetches names of persons as a string.
- **isUnderage:** Checks if there are any underage persons.
- **increaseAgeByTenPercent:** Increases the age of persons by 10%.

### POST, PUT, DELETE Requests

- **addPerson(personDTO):** Adds a new person to the server.
- **deletePerson(id):** Deletes a person by ID.
- **updatePerson(personDTO, id):** Updates a person's information by ID.

### Additional Features

- **deleteDuplicate:** Deletes duplicate records on the server.
- **getStatistic:** Fetches statistics about persons.

In `PersonHttpClientApplication`, response logs from the `ms-person-module` REST server are written, displaying response status and body.

## ms-person-module

The `ms-person-module` includes the addition of the `LoadDatabase` class for loading data and the `PersonRestController` for handling REST endpoints. Below are the details of these changes.

### LoadDatabase

The `LoadDatabase` class initializes the database with sample data. It uses Spring Boot's `CommandLineRunner` to run the data initialization logic when the application starts. Sample data includes `Person` objects with names and ages.

### PersonRestController

The `PersonRestController` class handles REST endpoints related to `Person` entities. It provides endpoints for creating, updating, deleting, and querying `Person` objects. Additionally, it offers endpoints for various operations on `Person` objects, such as filtering, sorting, and statistical analysis.

The controller uses validation annotations for input data and handles exceptions by returning appropriate error responses. It also converts between `Person` entities and `PersonDTO` objects using the `PersonDTOMapper` class.

The controller includes exception handling for `PersonNotFoundException` and `PersonNotCreatedException`.

