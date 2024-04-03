package org.kharitonov.ms.person.service.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.person.repository.PersonRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
public class SchedulerService {

    private final PersonRepo personRepo;

    public SchedulerService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Scheduled(fixedDelayString = "${schedulerService.showPersonCount.intervalInMillis}")
    public void showPersonCount() {
        log.info("Table \"Persons\" {} - count.", personRepo.count());
    }
}

