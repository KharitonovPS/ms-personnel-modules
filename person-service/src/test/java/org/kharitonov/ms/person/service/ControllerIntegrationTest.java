package org.kharitonov.ms.person.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kharitonov.ms.person.service.domain.person.entity.Person;
import org.kharitonov.ms.person.service.domain.person.repository.PersonRepo;
import org.kharitonov.ms.person.service.service.QueueService;
import org.kharitonov.person.http.client.PersonClientImpl;
import org.kharitonov.person.http.client.util.CustomPageImpl;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
public class ControllerIntegrationTest extends AbstractIntegrationServiceTest {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private QueueService queueService;

    @LocalServerPort
    private int port;

    private PersonClientImpl personClientImpl;


    @BeforeEach
    void doIt() {
        personRepo.deleteAll();
        Person alice = new Person("Alice", 25);
        alice.setId(null);
        log.info("Preloading " + personRepo.save(alice));
        log.info("Preloading " + personRepo.save(new Person("Bob", 30)));
        log.info("Preloading " + personRepo.save(new Person("David", 28)));
        log.info("Preloading " + personRepo.save(new Person("Davis", 38)));
        log.info("Preloading " + personRepo.save(new Person("Eve", 35)));

        if (personClientImpl == null) {
            personClientImpl = new PersonClientImpl(port, 1);
        }
    }

    @Test
    public void personControllerFindAllTest() {
        CustomPageImpl<PersonDTO> personDTOS = personClientImpl.findAllPerson();
        assertNotNull(personDTOS);
        assertEquals(5, personDTOS.getNumberOfElements());
        assertNotNull(personDTOS.getContent());
        PersonDTO testPerson = new PersonDTO();
        testPerson.setName("Alice");
        testPerson.setAge(25);
        assertEquals(personDTOS.getContent().get(0).getName(), testPerson.getName());
        assertEquals(personDTOS.getContent().get(0).getAge(), testPerson.getAge());
    }

    @Test
    public void personControllerFindPageTest() {
        CustomPageImpl<PersonDTO> personDTOS = personClientImpl.findAllPerson(1, 2);
        assertNotNull(personDTOS);
        assertEquals(2, personDTOS.getNumberOfElements());
        PersonDTO testPerson = new PersonDTO();
        testPerson.setName("David");
        testPerson.setAge(28);
        assertEquals(personDTOS.getContent().get(0).getName(), testPerson.getName());
        assertEquals(personDTOS.getContent().get(0).getAge(), testPerson.getAge());
    }

    @Test
    public void personControllerFindByNameTest() {
        PersonDTO personDTO = personClientImpl.findByName("Alice");
        assertEquals("Alice", personDTO.getName());
    }

    @Test
    public void personControllerFindByNonExisingNameTest() {
        String randomName = RandomStringUtils.randomAlphabetic(7);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClientImpl.findByName(randomName)
        );
        assertTrue(runtimeException.getMessage().contains("HTTP request failed"));
    }

    @Test
    public void personControllerCreatePersonTest() throws JsonProcessingException, InterruptedException {
        String name = RandomStringUtils.randomAlphabetic(12);
        String response = personClientImpl.create(new PersonDTO(null, name, 12));
        log.info(response);
        Thread.sleep(6000);
        assertEquals(response, "\"CREATED\"");
        assertTrue(personRepo.findByName(name).isPresent());
    }

    @Test
    public void personControllerCreateNotValidPersonTest() {
        String longName = RandomStringUtils.randomAlphabetic(111);
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClientImpl.create(new PersonDTO(null, longName, 1112))
        );
        assertTrue(runtimeException.getMessage().contains("\"status\":400"));
    }

    @Test
    public void personControllerDeleteTest() {
        PersonDTO dto = personClientImpl.findByName("Alice");
        String response = personClientImpl.deletePerson(dto.getId());
        assertFalse(personRepo.findByName(dto.getName()).isPresent());
        assertEquals(response, "\"OK\"");
    }

    @Test
    public void personControllerPutRequestTest() throws JsonProcessingException {
        PersonDTO dto = personClientImpl.findByName("Alice");
        var name = "updated";
        String response = personClientImpl.updatePerson(new PersonDTO(dto.getId(), name, 15));
        assertEquals(response, "\"OK\"");
        List<Person> person = personRepo.findAll();
        PersonDTO findAfterUpdate = personClientImpl.findByName(name);
        assertEquals(findAfterUpdate.getName(), name);
    }

    @Test
    public void personControllerPutRequestNonValidParamTest() {
        PersonDTO dto = personClientImpl.findByName("Alice");
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> personClientImpl.updatePerson(new PersonDTO(dto.getId(), "sad", 11111111)));
        assertTrue(runtimeException.getMessage().contains("HTTP request failed"));
    }

    @SneakyThrows
    @Test
    public void personControllerCreateWithHQueue() throws JsonProcessingException {
        long size = 200;
        List<Person> personList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            personClientImpl.create(new PersonDTO(null, "Name" + i, 1));
        }
        Thread.sleep(30000);
        assertEquals(size + 5, personRepo.count());
    }

    @SneakyThrows
    @Test
    public void savePersonWithExistName() throws JsonProcessingException {
        var name = "same";
        PersonDTO person = new PersonDTO(null, name, 1);
        PersonDTO person2 = new PersonDTO(null, "Unique", 2);
        PersonDTO person3 = new PersonDTO(null, name, 3);
        PersonDTO person4 = new PersonDTO(null, "David", 4);
        List<PersonDTO> personList = new ArrayList<>();
        personList.add(person);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);
        for (PersonDTO people : personList) {
            try {
                Thread.sleep(300);
                personClientImpl.create(people);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(10000);
        assertEquals(7, personRepo.count());
    }
}