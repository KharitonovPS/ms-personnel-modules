package org.kharitonov.ms.person.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


}
