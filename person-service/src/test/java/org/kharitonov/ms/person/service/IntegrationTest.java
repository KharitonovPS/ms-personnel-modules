package org.kharitonov.ms.person.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RequiredArgsConstructor
public class IntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

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
    public void getByIdPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/persons/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andReturn();
        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    public void getByNonExistedIdPersonControllerTest() throws Exception {
        long id = 100L;
        this.mockMvc
                .perform(get("/persons/" + id))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof PersonNotFoundException))
                .andExpect(jsonPath("$.message").value("Could not find person with id - " + id))
                .andReturn();
    }

    @Test
    public void getAllPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/persons"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    public void getAllByPagesPersonControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/persons?page=2&size=1"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray contentArray = jsonResponse.getJSONArray("content");

        boolean found = false;

        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject person = contentArray.getJSONObject(i);
            if (person.getString("name").equals("David") && person.getInt("age") == 28) {
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found, "Expected value not found in 'content' array.");
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

        Assertions.assertEquals("\"CREATED\"", mvcResult.getResponse().getContentAsString());

    }
    public static String asJsonString(Person person) {
        try {
            return new ObjectMapper().writeValueAsString(person);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        Assertions.assertEquals("Invalid request content.", mvcResult.getResponse().getErrorMessage());

    }



}