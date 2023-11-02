package org.kharitonov.ms.person.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.person.http.client.PersonClient;
import org.kharitonov.person.http.client.util.CustomPageImpl;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
public class ControllerIntegrationTest extends AbstractIntegrationServiceTest {

    @Autowired
    private PersonRepo personRepo;

    @LocalServerPort
    private int port;

    private PersonClient personClient;


    @BeforeEach
    void doIt() {
        personRepo.deleteAll();

        log.info("Preloading " + personRepo.save(new Person("Alice", 25)));
        log.info("Preloading " + personRepo.save(new Person("Bob", 30)));
        log.info("Preloading " + personRepo.save(new Person("David", 28)));
        log.info("Preloading " + personRepo.save(new Person("David", 38)));
        log.info("Preloading " + personRepo.save(new Person("Eve", 35)));

        if (personClient == null) {
            personClient = new PersonClient(port);
        }
    }

    @Test
    public void personControllerFindAllTest() {
        CustomPageImpl<PersonDTO> personDTOS = personClient.findAllPerson();
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
        CustomPageImpl<PersonDTO> personDTOS = personClient.findAllPerson(1, 2);
        assertNotNull(personDTOS);
        assertEquals(2, personDTOS.getNumberOfElements());
        PersonDTO testPerson = new PersonDTO();
        testPerson.setName("David");
        testPerson.setAge(28);
        assertEquals(personDTOS.getContent().get(0), testPerson);
    }

    @Test
    public void personControllerFindByNameTest() {
        PersonDTO personDTO = personClient.getPerson("Alice");
        assertEquals("Alice", personDTO.getName());
    }

    @Test
    public void personControllerFindByNonExisingNameTest() {
        String randomName = RandomStringUtils.randomAlphabetic(7);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClient.getPerson(randomName)
        );
        assertTrue(runtimeException.getMessage().contains("HTTP request failed"));
    }

    @Test
    public void personControllerCreatePersonTest() throws JsonProcessingException {
        String name = RandomStringUtils.randomAlphabetic(12);
        String response = personClient.addPerson(name, 11);
        log.info(response);
        assertEquals(response, "\"CREATED\"");
        assertTrue(personRepo.findByName(name).isPresent());
        log.info(personRepo.findByName(name).toString());
    }

    @Test
    public void personControllerCreateNotValidPersonTest() {
        String longName = RandomStringUtils.randomAlphabetic(111);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClient.addPerson(longName, 1112)
        );
        assertTrue(runtimeException.getMessage().contains("\"status\":400"));
    }

    @Test
    public void personControllerDeleteTest() {
        Long id = 7L;
        Optional<Person> person = personRepo.findById(id);
        String response = personClient.deletePerson(id);
        assertFalse(personRepo.findByName(person.get().getName()).isPresent());
        assertEquals(response, "\"OK\"");
        List<Person> resultlist = personRepo.findAll();
        for (Person findedPerson : resultlist) {
            log.info(findedPerson.getName());
        }
    }

    @Test
    public void personControllerPutRequestTest() throws JsonProcessingException {
        String response = personClient.updatePerson("Sadik", 11, 1);
        assertEquals(response, "\"OK\"");
        List<Person> person = personRepo.findAll();
        for (Person findedPerson : person) {
            log.info(findedPerson.getName());
        }
    }

    @Test
    public void personControllerPutRequestNonValidParamTest() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClient.updatePerson("Sadik", 1111, 1));
        assertTrue(runtimeException.getMessage().contains("HTTP request failed"));
    }
}