package org.kharitonov.ms.person.service.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDTOMapper personDTOMapper;
    private final QueueService queueService;


    public void addPersonToQueue(PersonDTO personDTO) {
        queueService.addToQueue(personDTO);
    }

    public void save() {
        List<Person> resultList = new ArrayList<>();
        int batchSize = 50;
        if (queueService.getPersonDTOQueue().size() >= batchSize) {
            while (!queueService.isEmpty()) {
                resultList = queueService.getPersonDTOQueue()
                        .stream()
                        .map(person -> queueService.getFromQueue())
                        .map(personDTOMapper::dtoToPerson)
                        .collect(Collectors.toList());
            }
            personRepo.saveAllAndFlush(resultList);
            resultList.clear();
        }
    }

    public void updatePerson(Long id, PersonDTO personDTO) {
        Person newPerson = personDTOMapper.dtoToPerson(personDTO);
        personRepo.findById(id)
                .map(person -> {
                    person.setName(newPerson.getName());
                    person.setAge(newPerson.getAge());
                    person.setUpdatedAt(LocalDateTime.now());
                    return personRepo.save(person);
                })
                .orElseGet(() -> personRepo.save(newPerson));
    }

    public void deleteById(Long id) {
        personRepo.deleteById(id);
    }

    public Page<PersonDTO> getPages(Pageable pageable) {
        Page<Person> personPage = personRepo.findAll(pageable);
        List<PersonDTO> personDTOList = personPage
                .stream()
                .map(personDTOMapper::personToDto)
                .toList();
        return new PageImpl<>(personDTOList);
    }

    public PersonDTO getElementByName(String name) {
        Person findPerson = personRepo
                .findByName(name)
                .orElseThrow(() -> new PersonNotFoundException(name)
                );
        return personDTOMapper.personToDto(findPerson);
    }
}
