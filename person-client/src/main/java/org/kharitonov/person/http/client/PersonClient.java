package org.kharitonov.person.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.kharitonov.person.model.dto.PersonDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class PersonClient {

    private static final String BASE_URL = "http://localhost:8080/persons";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public PersonClient() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }
    public List<PersonDTO> findAllPerson() {
        HttpRequest request = createGetRequest(BASE_URL);
        String response = sendRequest(request);
        return deserializeList(response);
    }

    public PersonDTO getPerson(int id) {
        HttpRequest request = createGetRequest(BASE_URL + "/" + id);
        String response = sendRequest(request);
        return deserialize(response);
    }

    public List<PersonDTO> filterMultiplyEven() {
        HttpRequest request = createGetRequest(BASE_URL + "/filterMultiplyEven");
        String response = sendRequest(request);
        return deserializeList(response);
    }

    public List<PersonDTO> sortByAge() {
        HttpRequest request = createGetRequest(BASE_URL + "/sortByAge");
        String response = sendRequest(request);
        return deserializeList(response);
    }

    public LinkedHashMap<Character, List<PersonDTO>> sortByFirstCharOfName() {
        HttpRequest request = createGetRequest(BASE_URL + "/sortByFirstCharOfName");
        String response = sendRequest(request);
        return deserializeLinkedHashMap(response);
    }

    public PersonDTO findMaxAge() {
        HttpRequest request = createGetRequest(BASE_URL + "/findMaxAge");
        String response = sendRequest(request);
        return deserialize(response);
    }

    public List<PersonDTO> skipAndLimitation() {
        HttpRequest request = createGetRequest(BASE_URL + "/skipAndLimitation");
        String response = sendRequest(request);
        return deserializeList(response);
    }

    public String personNamesAsString() {
        HttpRequest request = createGetRequest(BASE_URL + "/personNamesAsString");
        return sendRequest(request);
    }

    public String isUnderage() {
        HttpRequest request = createGetRequest(BASE_URL + "/isUnderage");
        return sendRequest(request);
    }

    public List<PersonDTO> increaseAgeByTenPercent() {
        HttpRequest request = createGetRequest(BASE_URL + "/increaseAgeByTenPercent");
        String response = sendRequest(request);
        return deserializeList(response);
    }

    @SneakyThrows
    public String deleteDuplicate() {
        HttpRequest request = createGetRequest(BASE_URL + "/deleteDuplicate");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return String.valueOf(response.statusCode());
    }

    public Map<String, Integer> getStatistic() {
        HttpRequest request = createGetRequest(BASE_URL + "/getStatistic");
        String response = sendRequest(request);
        return deserializeMap(response);
    }


    public String addPerson(int age, String name) throws JsonProcessingException {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAge(age);
        personDTO.setName(name);
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personDTO);
        HttpRequest request = createPostRequest(BASE_URL, personAsString);
        return sendRequest(request);
    }

    public String deletePerson(int id) {
        String url = PersonClient.BASE_URL + "/" + id;
        HttpRequest request = createDeleteRequest(url);
        return sendRequest(request);
    }

    public String updatePerson(int age, String name, int id) throws JsonProcessingException {
        String url = PersonClient.BASE_URL + "/" + id;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setAge(age);
        String personAsString = objectMapper.writeValueAsString(personDTO);
        HttpRequest request = createPutRequest(url, personAsString);
        return sendRequest(request);
    }

    public HttpRequest createGetRequest(String url) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url))
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

    private List<PersonDTO> deserializeList(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<List<PersonDTO>>() {
                    });
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to deserialize response: " + exception.getMessage(), exception);
        }
    }

    private LinkedHashMap<Character, List<PersonDTO>> deserializeLinkedHashMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<LinkedHashMap<Character, List<PersonDTO>>>() {
            });
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to deserialize response: " + exception.getMessage(), exception);
        }
    }

    private Map<String, Integer> deserializeMap(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<Map<String, Integer>>() {
                    });
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to deserialize response: " + exception.getMessage(), exception);
        }
    }
}
