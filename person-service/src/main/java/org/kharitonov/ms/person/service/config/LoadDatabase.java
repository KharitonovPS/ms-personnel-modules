package org.kharitonov.ms.person.service.config;

import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(PersonRepo personRepo) {
        if(personRepo.count()==0){
            return args -> {
                log.info("Preloading " + personRepo.save(new Person("Alice", 25)));
                log.info("Preloading " + personRepo.save(new Person("Bob", 30)));
                log.info("Preloading " + personRepo.save(new Person("David", 28)));
                log.info("Preloading " + personRepo.save(new Person("David", 38)));
                log.info("Preloading " + personRepo.save(new Person("Eve", 35)));
            };
        }
        return null;
    }
}

