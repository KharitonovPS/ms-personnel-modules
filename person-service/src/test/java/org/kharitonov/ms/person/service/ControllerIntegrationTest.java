package org.kharitonov.ms.person.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.person.http.client.PersonClient;
import org.kharitonov.person.http.client.util.CustomPageImpl;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RequiredArgsConstructor
public class ControllerIntegrationTest extends AbstractIntegrationServiceTest {

    @Autowired
    private PersonRepo personRepo;

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private final PersonClient personClient = new PersonClient();


    @BeforeEach
    void doIt() {
        personRepo.deleteAll();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        log.info("Preloading " + personRepo.save(new Person("Alice", 25)));
        log.info("Preloading " + personRepo.save(new Person("Bob", 30)));
        log.info("Preloading " + personRepo.save(new Person("David", 28)));
        log.info("Preloading " + personRepo.save(new Person("David", 38)));
        log.info("Preloading " + personRepo.save(new Person("Eve", 35)));
    }

    @Test
    public void getAllByPagesPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/persons?page=2&size=1"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();


        Boolean found = responseContainsValue(mvcResult, "David", 28);
        assertTrue(found, "Expected value not found in 'content' array.");
    }

    @Test
    public void postRequestPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/persons")
                        .content(asJsonString(new Person("Josh", 67)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        assertEquals("\"CREATED\"", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void postRequestWithNotValidValuesPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(post("/persons")
                        .content(asJsonString(new Person("Josh", 888)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals("Invalid request content.", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    public void putRequestPersonControllerTest() throws Exception {
        this.mockMvc
                .perform(put("/persons/999")
                        .content(asJsonString(new Person("TestBoy", 1)))
                        .contentType(MediaType.APPLICATION_JSON));

        MvcResult getAllRequest = this.mockMvc
                .perform(get("/persons"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Boolean found = responseContainsValue(getAllRequest, "TestBoy", 1);
        assertTrue(found, "Expected value not found in 'content' array.");
    }

    @Test
    public void putRequestWithNotValidValuesPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(put("/persons/1")
                        .content(asJsonString(new Person("NotValidAge", 1199)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals("Invalid request content.", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    public void deleteRequestPersonControllerTest() throws Exception {
        this.mockMvc
                .perform(delete("/persons/11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
        MvcResult getAllRequest = this.mockMvc
                .perform(get("/persons"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        assertFalse(responseContainsValue(getAllRequest, "Alice", 25));
    }


    @Test
    public void personControllerFindAllTest() {
        CustomPageImpl<PersonDTO> personDTOS = personClient.findAllPerson(port, "/persons");
        assertNotNull(personDTOS);
        assertEquals(5, personDTOS.getNumberOfElements());
        assertNotNull(personDTOS.getContent());
        PersonDTO testPerson = new PersonDTO();
        testPerson.setName("Alice");
        testPerson.setAge(25);
        assertEquals(personDTOS.getContent().get(0), testPerson);
    }

    @Test
    public void personControllerFindPageTest() {
        CustomPageImpl<PersonDTO> personDTOS = personClient.findAllPerson(port, "/persons?page=1&size=2");
        assertNotNull(personDTOS);
        assertEquals(2, personDTOS.getNumberOfElements());
        PersonDTO testPerson = new PersonDTO();
        testPerson.setName("David");
        testPerson.setAge(28);
        assertEquals(personDTOS.getContent().get(0), testPerson);
    }

    @Test
    public void personControllerFindByNameTest() {
        PersonDTO personDTO = personClient.getPerson(port, "/persons/Alice");
        assertEquals("Alice", personDTO.getName());
    }

    @Test
    public void personControllerFindByNonExisingNameTest() {
        String randomName = RandomStringUtils.randomAlphabetic(7);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClient.getPerson(port, "/persons/" + randomName)
        );
        assertTrue(runtimeException.getMessage().contains("HTTP request failed with status code:"));
    }


    public static Boolean responseContainsValue(MvcResult mvcResult, String name, int age)
            throws JSONException, UnsupportedEncodingException {
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray contentArray = jsonResponse.getJSONArray("content");

        boolean found = false;

        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject person = contentArray.getJSONObject(i);
            if (person.getString("name").equals(name)
                    && person.getInt("age") == age) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static String asJsonString(Person person) {
        try {
            return new ObjectMapper().writeValueAsString(person);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}