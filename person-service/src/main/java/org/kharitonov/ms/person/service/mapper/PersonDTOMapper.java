package org.kharitonov.ms.person.service.mapper;

import lombok.Getter;
import lombok.Setter;
import org.kharitonov.ms.person.service.domain.Person;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class PersonDTOMapper {

    public Person dtoToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        enrichPerson(person);
        return person;
    }

    public PersonDTO personToDto(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAge(person.getAge());
        personDTO.setName(person.getName());
        return personDTO;
    }

    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
    }
}
