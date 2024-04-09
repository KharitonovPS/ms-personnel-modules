package org.kharitonov.ms.person.service.service;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.person.entity.Person;
import org.kharitonov.ms.person.service.domain.person.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.domain.person.service.PersonService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            List<Person> personList = queueService.getPersonsFromQueue()
                    .stream()
                    .map(personDTOMapper::toPerson)
                    .toList();
            if (!personList.isEmpty()){
                personService.saveAll(filterDuplicates(personList));
            }
        } catch (Exception e) {
            log.error("read queue:", e);
        }
    }

    private List<Person> filterDuplicates(List<Person> listFromQueue) {
        Set<String> existingNames = new HashSet<>();
        List<Person> uniquePerson = new ArrayList<>();

        for (Person person : listFromQueue) {
            if (!existingNames.contains(person.getName())) {
                existingNames.add(person.getName());
                uniquePerson.add(person);
            } else {
                log.error("duplicate key value violates unique constraint \"idx_name\","
                        + ": {} already exist in current batch", person.getName());
            }
        }

        List<Person> personsFromRepo = personService.findAllByName(
                existingNames.stream().toList()
        );
        List<String> personsFromRepoNames = personsFromRepo.stream()
                .map(Person::getName).toList();
        List<Person> resultList = new ArrayList<>();

        for (Person person : uniquePerson) {
            if (!personsFromRepoNames.contains(person.getName())) {
                resultList.add(person);
            } else {
                log.error("duplicate key value violates unique constraint \"idx_name\","
                        + ": {} already exist in database", person.getName());
            }
        }
        return resultList;
    }
}
