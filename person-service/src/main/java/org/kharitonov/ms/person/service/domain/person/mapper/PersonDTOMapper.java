package org.kharitonov.ms.person.service.domain.person.mapper;

import lombok.Getter;
import lombok.Setter;
import org.kharitonov.ms.person.service.domain.person.entity.Person;
import org.kharitonov.person.model.dto.PersonDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.kharitonov.ms.person.service.domain.AuditMetadata.SYSTEM_USER;

@Getter
@Setter
@Component
public class PersonDTOMapper {

    public Person toPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setId(personDTO.getId());
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        enrichPerson(person);
        return person;
    }

    public PersonDTO toDto(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setAge(person.getAge());
        personDTO.setName(person.getName());
        return personDTO;
    }

    private void enrichPerson(Person person) {
        person.setCreatedAt(Instant.now());
        person.setUpdatedAt(Instant.now());
        person.setUpdatedBy(SYSTEM_USER);
        person.setCreatedBy(SYSTEM_USER);
    }
}
