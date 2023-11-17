package org.kharitonov.ms.person.service.service;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;

@Component
@Data
@RequiredArgsConstructor
@Slf4j
public class QueueListener {

    private final PersonService personService;
    private final QueueService queueService;
    private final PersonDTOMapper personDTOMapper;

    @PostConstruct
    @Scheduled(fixedDelayString = "${queueListener.persistQueueData.intervalInMillis}")
    public void persistQueueData() {
        this.readQueue();
    }

    private void readQueue() {
        try {
            List<Person> personList = queueService
                    .getPersonsFromQueue()
                    .stream()
                    .map(personDTOMapper::dtoToPerson)
                    .toList();
            personService.saveAll(checkExistPersons(personList));
        } catch (Exception e) {
            log.error("read queue:", e);
        }
    }

    private List<Person> checkExistPersons(List<Person> listFromQueue) {
        LinkedHashSet<Person> personSet = new LinkedHashSet<>();
        List<String> names = listFromQueue
                .stream()
                .map(Person::getName)
                .toList();
        List<Person> existingPersons = personService.findAllByName(names);
        for (Person person : listFromQueue) {
            if (!existingPersons.contains(person)) {
                if (personSet.add(person)) {
                    personSet.add(person);
                } else {
                    log.error("duplicate key value violates unique constraint \"idx_name\"," +
                            ": {} already exist in current batch", person.getName());
                }
            } else {
                log.error("duplicate key value violates unique constraint \"idx_name\"," +
                        ": {} already exist in database", person.getName());
            }
        }
        return personSet.stream().toList();
    }
}
