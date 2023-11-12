package org.kharitonov.ms.person.service.config;

import org.kharitonov.ms.person.service.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSaverInit implements CommandLineRunner {

    private final PersonService personService;

    public DataSaverInit(PersonService personService) {
        this.personService = personService;
    }
    @Override
    public void run(String... args) throws Exception {
        personService.savePersonFromQueue();
    }
}

