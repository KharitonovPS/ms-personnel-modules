package org.kharitonov.person.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.kharitonov.person.http.client.util.ClientRequestHelper;
import org.kharitonov.person.http.client.util.CustomPageImpl;
import org.kharitonov.person.model.dto.PersonDTO;

import java.net.http.HttpRequest;

@Getter
@RequiredArgsConstructor
@Setter
public class PersonClientImpl implements PersonClient {

    private final String BASE_URI;
    private final ObjectMapper objectMapper;
    private ClientRequestHelper clientRequestHelper;

    private static final String PERSON_API = "/api/v1/persons";

    public PersonClientImpl(int port, int timeoutSeconds) {
        this.objectMapper = new ObjectMapper();
        this.BASE_URI = "http://localhost:" + port + PERSON_API;
        this.clientRequestHelper = new ClientRequestHelper(timeoutSeconds);
    }

    public CustomPageImpl<PersonDTO> findAllPerson() {
        HttpRequest request = clientRequestHelper
                .createGetRequest(BASE_URI);
        String response = clientRequestHelper
                .sendRequest(request);
        return clientRequestHelper.deserializeList(response);
    }

    public CustomPageImpl<PersonDTO> findAllPerson(int page, int size) {
        HttpRequest request = clientRequestHelper.
                createGetRequest(BASE_URI, page, size);
        String response = clientRequestHelper
                .sendRequest(request);

        return clientRequestHelper.deserializeList(response);
    }

    public PersonDTO findByName(String name) {
        HttpRequest request = clientRequestHelper
                .createGetRequest(BASE_URI + "/like/", name);
        String response = clientRequestHelper
                .sendRequest(request);

        return clientRequestHelper.deserialize(response);
    }

    @SneakyThrows
    public String create(PersonDTO personDTO) {
        String personAsString = objectMapper
                .writeValueAsString(personDTO);
        HttpRequest request = clientRequestHelper
                .createPostRequest(
                        BASE_URI, personAsString
                );
        return clientRequestHelper.sendRequest(request);
    }

    public String deletePerson(Long id) {
        String url = BASE_URI + "/" + id;
        HttpRequest request = clientRequestHelper
                .createDeleteRequest(url);
        return clientRequestHelper.sendRequest(request);
    }

    @SneakyThrows
    public String updatePerson(PersonDTO personDTO) {
        String url = BASE_URI + "/" + personDTO.getId();

        String personAsString = objectMapper
                .writeValueAsString(personDTO);

        HttpRequest request = clientRequestHelper
                .createPutRequest(url, personAsString);

        return clientRequestHelper.sendRequest(request);
    }
}
