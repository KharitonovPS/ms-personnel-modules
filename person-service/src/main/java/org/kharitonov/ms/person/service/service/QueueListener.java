package org.kharitonov.ms.person.service.service;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Data
@RequiredArgsConstructor
@Slf4j
public class QueueListener {

    private final PersonService personService;
    private final QueueService queueService;
    private final PersonDTOMapper personDTOMapper;


    private static final ScheduledExecutorService
            executorService =
            Executors.newSingleThreadScheduledExecutor(
                    runnable -> {
                        var thread = new Thread(runnable);
                        thread.setName("asm-publisher");
                        return thread;
                    }
            );

    @PostConstruct
    public void start() {
        executorService.scheduleWithFixedDelay(
                this::readQueue,
                0,
                1500,
                TimeUnit.MILLISECONDS
        );
    }

    private void readQueue() {
        try {
            List<Person> personList = queueService.getPersonsFromQueue()
                    .stream()
                    .map(personDTOMapper::dtoToPerson)
                    .toList();
            personService.saveAll(personList);
        } catch (Exception e) {
            log.error("read queue:", e);
        }
    }
}
