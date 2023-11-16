package org.kharitonov.ms.person.service.service;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
            personService.saveAll(personList);
        } catch (Exception e) {
            log.error("read queue:", e);
        }
    }
}
