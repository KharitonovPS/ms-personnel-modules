package org.kharitonov.ms.person.service.util;

import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.service.QueueService;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@NoArgsConstructor
@Component
@Data
public class DataSaverThread implements Runnable {


    private PersonRepo personRepo;
    private QueueService queueService;
    private PersonDTOMapper personDTOMapper;

    public DataSaverThread(PersonRepo personRepo, QueueService queueService, PersonDTOMapper personDTOMapper) {
        this.personRepo = personRepo;
        this.queueService = queueService;
        this.personDTOMapper = personDTOMapper;
    }

    private volatile boolean running = true;

    @Override
    public void run() {
        log.info("Thread started - " + Thread.currentThread().getName());
        while (running) {
            if (queueService.getPersonQueue().isEmpty()) {
                try {
                    queueService.waitIsNotEmpty();
                } catch (InterruptedException e) {
                    log.info("Error while waiting to Consume data.");
                    break;
                }
            }
            List<Person> personList;
            try {
                personList = queueService.getPersonsFromQueue()
                        .stream()
                        .map(personDTOMapper::dtoToPerson)
                        .toList();
                personRepo.saveAll(personList);
            } catch (RuntimeException ex) {
                ex.printStackTrace(System.err);
            }
        }
        log.info("Ended thread-" + Thread.currentThread().getName());
    }

    @PreDestroy
    public void stop() {
        running = false;
        log.info("Thread was destroyed.");
    }
}
