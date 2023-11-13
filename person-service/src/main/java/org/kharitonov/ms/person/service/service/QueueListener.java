package org.kharitonov.ms.person.service.service;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Component
@Data
@RequiredArgsConstructor
public class QueueListener {

    private final PersonRepo personRepo;
    private final QueueService queueService;
    private final PersonDTOMapper personDTOMapper;



    private static final ScheduledExecutorService
            executorService =
            Executors.newSingleThreadScheduledExecutor(runnable
                    -> {
                var thread = new Thread(runnable);
                thread.setName("asm-publisher");
                return thread;
            });

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
            personRepo.saveAll(personList);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
