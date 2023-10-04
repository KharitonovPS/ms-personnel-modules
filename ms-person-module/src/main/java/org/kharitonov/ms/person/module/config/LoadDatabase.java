package org.kharitonov.ms.person.module.config;

import org.kharitonov.ms.person.module.domain.Person;
import org.kharitonov.ms.person.module.repository.PersonRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PersonRepo personRepo) {

        return args -> {
            log.info("Preloading " + personRepo.save(new Person("Alice", 25)));
            log.info("Preloading " + personRepo.save(new Person("Bob", 30)));
            log.info("Preloading " + personRepo.save(new Person("David", 28)));
            log.info("Preloading " + personRepo.save(new Person("David", 38)));
            log.info("Preloading " + personRepo.save(new Person("Eve", 35)));
        };
    }
}

