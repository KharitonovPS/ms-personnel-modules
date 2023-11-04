package org.kharitonov.ms.person.service.service;

import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerService {

    private final PersonRepo personRepo;

    public SchedulerService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Scheduled(fixedDelay = 10000L)
    public void schedulerEveryTenSeconds(){
        log.info("Table \"Persons\" {} - count.", personRepo.count());
    }
}
