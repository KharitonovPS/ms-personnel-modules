package org.kharitonov.ms.person.service.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
@Getter
public class QueueService {

    private final Queue<PersonDTO> personDTOS = new ConcurrentLinkedQueue<>();

    public void addToQueue(PersonDTO personDTO) {
        personDTOS.add(personDTO);
    }

    public PersonDTO getFromQueue() {
        return personDTOS.remove();
    }

    public boolean isEmpty() {
        return personDTOS.isEmpty();
    }

}
