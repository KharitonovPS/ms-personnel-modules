package org.kharitonov.ms.person.service.service;

import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class QueueService {
    
    private Queue<PersonDTO> personDTOS = new ConcurrentLinkedQueue<>();
    public void addToQueue(PersonDTO personDTO){
        personDTOS.add(personDTO);
    }
    
    public PersonDTO getFromQueue(){
        return personDTOS.poll();
    }
}
