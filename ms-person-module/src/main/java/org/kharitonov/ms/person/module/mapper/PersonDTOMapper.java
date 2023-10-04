package org.kharitonov.ms.person.module.mapper;

import lombok.Getter;
import lombok.Setter;
import org.kharitonov.ms.person.module.domain.Person;
import org.kharitonov.ms.person.module.dto.PersonDTO;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PersonDTOMapper {

    public Person dtoToPerson(PersonDTO personDTO){
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        return person;
    }

    public PersonDTO personToDto(Person person){
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAge(person.getAge());
        personDTO.setName(person.getName());
        return personDTO;
    }
}