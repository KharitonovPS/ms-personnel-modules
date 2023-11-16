package org.kharitonov.ms.person.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.exceptions.PersonNotFoundException;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDTOMapper personDTOMapper;
    private final QueueService queueService;


    public void addPersonToQueue(PersonDTO personDTO) {
        queueService.addToQueue(personDTO);
    }

    public void updatePerson(Long id, PersonDTO personDTO) {
        if (personRepo.findById(id).isEmpty()) {
            queueService.addToQueue(personDTO);
        } else {
            personRepo.findById(id).map(person -> {
                        person.setName(personDTO.getName());
                        person.setAge(personDTO.getAge());
                        person.setUpdatedAt(LocalDateTime.now());
                        return personRepo.save(person);
                    }
            );
        }
    }

    public void deleteById(Long id) {
        personRepo.deleteById(id);
    }

    public void saveAll(List<Person> list) {
        personRepo.saveAll(list);
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
        Person findPerson = personRepo.findByName(name).orElseThrow(() -> new PersonNotFoundException(name));
        return personDTOMapper.personToDto(findPerson);
    }
}
