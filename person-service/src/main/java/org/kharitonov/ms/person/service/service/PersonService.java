package org.kharitonov.ms.person.service.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.util.BindingResultMessageBuilder;
import org.kharitonov.ms.person.service.util.PersonNotCreatedException;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDTOMapper personDTOMapper;

    public void save(PersonDTO personDTO) {
        Person person = enrichPerson(
                personDTOMapper
                        .dtoToPerson(personDTO)
        );
        personRepo.save(person);
    }


    private Person enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
        return person;
    }

    public List<Person> findAll() {
        return personRepo.findAll();
    }

    public Person findById(long id) {
        return personRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    public Person updatePerson(Long id, Person newPerson) {

        return personRepo.findById(id)
                .map(person -> {
                    person.setName(newPerson.getName());
                    person.setAge(newPerson.getAge());
                    person.setUpdatedAt(LocalDateTime.now());
                    return personRepo.save(person);
                })
                .orElseGet(() -> {
                    enrichPerson(newPerson);
                    return personRepo.save(newPerson);
                });
    }

    public void deleteById(Long id) {
        personRepo.deleteById(id);
    }

    public PersonDTO getById(Long id) {
        return personDTOMapper.personToDto
                (personRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id)
                ));
    }
}
