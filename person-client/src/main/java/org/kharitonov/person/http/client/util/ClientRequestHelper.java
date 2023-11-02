package org.kharitonov.person.http.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kharitonov.person.model.dto.PersonDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ClientRequestHelper {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public ClientRequestHelper(int timeoutSeconds) {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    public HttpRequest createGetRequest(String url) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url + "/persons"))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createGetRequest(String url, String name) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url + "/persons/" + name))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createGetRequest(String url, int page, int size) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(url + "/persons?page=" + page + "&size=" + size))
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

    public String sendRequest(HttpRequest request) {
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

    public PersonDTO deserialize(String json) {
        try {
            return objectMapper.readValue(json, PersonDTO.class);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to deserialize response: " + exception.getMessage(), exception);
        }
    }

    public CustomPageImpl<PersonDTO> deserializeList(String json) {
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
