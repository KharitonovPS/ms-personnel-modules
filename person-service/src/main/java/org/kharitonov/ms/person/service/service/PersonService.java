package org.kharitonov.ms.person.service.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.ms.person.service.mapper.PersonDTOMapper;
import org.kharitonov.ms.person.service.repository.PersonRepo;
import org.kharitonov.ms.person.service.util.PersonNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;
    private final PersonDTOMapper personDTOMapper;

    public void save(Person person) {
        enrichPerson(person);
        personRepo.save(person);
    }

    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
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
}
