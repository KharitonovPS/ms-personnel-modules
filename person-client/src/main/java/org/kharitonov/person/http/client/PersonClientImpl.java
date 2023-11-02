package org.kharitonov.person.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

    public PersonClientImpl(int port, int timeoutSeconds) {
        this.objectMapper = new ObjectMapper();
        this.BASE_URI = "http://localhost:" + port;
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

    public PersonDTO getPerson(String name) {
        HttpRequest request = clientRequestHelper
                .createGetRequest(BASE_URI, name);
        String response = clientRequestHelper
                .sendRequest(request);

        return clientRequestHelper.deserialize(response);
    }

    public String addPerson(String name, int age)
            throws JsonProcessingException
    {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAge(age);
        personDTO.setName(name);
        String personAsString = objectMapper
                .writeValueAsString(personDTO);
        HttpRequest request = clientRequestHelper
                .createPostRequest(
                        BASE_URI + "/persons", personAsString
                );
        return clientRequestHelper.sendRequest(request);
    }

    public String deletePerson(Long id) {
        String url = BASE_URI + "/persons/" + id;
        HttpRequest request = clientRequestHelper
                .createDeleteRequest(url);
        return clientRequestHelper.sendRequest(request);
    }

    public String updatePerson(String name, int age, int id)
            throws JsonProcessingException {
        String url = BASE_URI + "/persons/" + id;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setAge(age);
        String personAsString = objectMapper
                .writeValueAsString(personDTO);

        HttpRequest request = clientRequestHelper
                .createPutRequest(url, personAsString);

        return clientRequestHelper.sendRequest(request);
    }
}
