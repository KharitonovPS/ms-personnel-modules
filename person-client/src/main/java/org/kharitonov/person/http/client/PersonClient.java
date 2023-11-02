package org.kharitonov.person.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.kharitonov.person.http.client.util.CustomPageImpl;
import org.kharitonov.person.model.dto.PersonDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Getter
@RequiredArgsConstructor
@Setter
public class PersonClient {

    private static final String BASE_URI = "http://localhost:";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private int port;

    public PersonClient() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

    public PersonClient(int port) {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.port = port;
    }

    public CustomPageImpl<PersonDTO> findAllPerson() {
        HttpRequest request = createGetRequest(port);
        String response = sendRequest(request);
        return deserializeList(response);
    }

    public CustomPageImpl<PersonDTO> findAllPerson(int page, int size) {
        HttpRequest request = createGetRequest(port, page, size);
        String response = sendRequest(request);
        return deserializeList(response);
    }

    public PersonDTO getPerson(String name) {
        HttpRequest request = createGetRequest(port, name);
        String response = sendRequest(request);
        return deserialize(response);
    }

    public String addPerson(String name, int age) throws JsonProcessingException {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAge(age);
        personDTO.setName(name);
        String personAsString = objectMapper.writeValueAsString(personDTO);
        HttpRequest request = createPostRequest(BASE_URI + port + "/persons", personAsString);
        return sendRequest(request);
    }

    public String deletePerson(Long id) {
        String url = BASE_URI + port + "/persons/" + id;
        HttpRequest request = createDeleteRequest(url);
        return sendRequest(request);
    }

    public String updatePerson(String name, int age, int id) throws JsonProcessingException {
        String url = BASE_URI + port + "/persons/" + id;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setAge(age);
        String personAsString = objectMapper.writeValueAsString(personDTO);
        HttpRequest request = createPutRequest(url, personAsString);
        return sendRequest(request);
    }

    public HttpRequest createGetRequest(int port) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(BASE_URI + port + "/persons"))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createGetRequest(int port, String name) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(BASE_URI + port + "/persons/" + name))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createGetRequest(int port, int page, int size) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(BASE_URI + port + "/persons?" + "page=" + page + "&size=" + size))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createDeleteRequest(String url) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .DELETE()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createPutRequest(String url, String body) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .headers("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createPostRequest(String url, String body) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("HTTP request failed with status code: " + response.statusCode()

                        + " " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private PersonDTO deserialize(String json) {
        try {
            return objectMapper.readValue(json, PersonDTO.class);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to deserialize response: " + exception.getMessage(), exception);
        }
    }

    private CustomPageImpl<PersonDTO> deserializeList(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<CustomPageImpl<PersonDTO>>() {
                    });
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to deserialize response: " + exception.getMessage(), exception);
        }
    }

}
