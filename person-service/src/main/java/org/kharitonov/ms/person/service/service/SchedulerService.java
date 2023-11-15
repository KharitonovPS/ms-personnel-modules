package org.kharitonov.ms.person.service.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Data
public class SchedulerService {

    private final PersonRepo personRepo;
    private final Long logFixedDelay;

    public SchedulerService(PersonRepo personRepo,
                            @Value("${scheduler.fixed.delay}") Long logFixedDelay) {
        this.personRepo = personRepo;
        this.logFixedDelay = logFixedDelay;
    }

    @Scheduled(fixedDelayString = "${scheduler.fixed.delay}")
    public void schedulerEveryTenSeconds(){
        log.info("Table \"Persons\" {} - count.", personRepo.count());
    }
}

