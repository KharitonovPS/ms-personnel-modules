package org.kharitonov.ms.person.service.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
@Getter
public class QueueService {

    private final PersonDTOMapper personDTOMapper;

    private final LinkedBlockingQueue<PersonDTO> personQueue = new LinkedBlockingQueue<>();

    public void addToQueue(PersonDTO personDTO) {
        personQueue.add(personDTO);
    }

    public List<PersonDTO> getPersonsFromQueue() {
        List<PersonDTO> resultList = new ArrayList<>();
        personQueue.drainTo(resultList, 50);
        return resultList;
    }
}
